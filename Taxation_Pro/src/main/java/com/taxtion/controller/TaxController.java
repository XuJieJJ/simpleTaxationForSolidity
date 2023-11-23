package com.taxtion.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.taxtion.Utils;
import com.taxtion.service.TaxService;
import com.taxtion.solidity.Invoice;
import com.taxtion.solidity.Payer;
import com.taxtion.solidity.TaxContract;
import com.taxtion.vo.InvoiceStatusVO;
import com.taxtion.vo.InvoiceVO;
import com.taxtion.vo.PayerVO;
import com.taxtion.vo.ResultVo;
import io.netty.util.internal.StringUtil;
import jdk.jshell.execution.Util;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.ABICodecException;
import org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderInterface;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderService;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.fisco.bcos.sdk.transaction.model.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 税务端controller
 */
@RestController
@RequestMapping("/tax")
@Slf4j
public class TaxController {
    @Autowired
    public TaxService taxService;

    @Autowired
    public Client client;





    /**
     * 保存用户地址信息
     */
    public CryptoKeyPair cryptoSuiteOfPayer;//纳税人
    public CryptoKeyPair cryptoSuiteOfTation;//管理员
    public  String ContractAddress;//合约地址
    public TaxController(){

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
        BcosSDK bcosSDK = context.getBean(BcosSDK.class);
        Client client1 = bcosSDK.getClient(1);
        //client=client1;
        CryptoKeyPair cryptoKeyPair1 = client1.getCryptoSuite().getCryptoKeyPair();
        try {
            TaxContract taxContract = TaxContract.deploy(client1, cryptoKeyPair1);
            String contractAddress = taxContract.getContractAddress();
            log.info("合约地址: {}",contractAddress);
            //保存公私钥
            cryptoSuiteOfTation = cryptoKeyPair1;
            ContractAddress = contractAddress;

        } catch (ContractException e) {
            throw new RuntimeException(e);
        }
    }

//=============================用户管理模块===========================================//
    /**
     * 创建纳税人
     * 前端页面显示输入用户信息
     * 输入完成之后跳转用户页面，不做用户判断 1-1用户
     */

    @PostMapping("/createPayer")
    public ResultVo createPayer(@RequestBody PayerVO payerVO) throws ContractException {
        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
        cryptoSuiteOfPayer = cryptoKeyPair;
        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoKeyPair);

        if (taxContract ==null) {
            return ResultVo.fail("合约不存在");

        }
        TransactionReceipt transactionReceipt = taxContract.AddPayers(payerVO.getName(), payerVO.getTaxID(), payerVO.getContactInfo());

        if (transactionReceipt.getStatus().equals("0x0")){
            return  ResultVo.success(transactionReceipt);
        }else {
            return ResultVo.fail(transactionReceipt.getStatusMsg());
        }
    }

    /**
     * 查询纳税人
     * @return
     * @throws ContractException
     */
    @GetMapping("/getPayer")
    public ResultVo getPayers() throws ContractException {
        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoSuiteOfPayer);

        Tuple3<String, String, String> payers = taxContract.getPayers(cryptoSuiteOfPayer.getAddress());
        if (payers ==null){
            return ResultVo.fail("请添加纳税人");
        }
        PayerVO payerVO = new PayerVO();
        payerVO.setName(payers.getValue1());
        payerVO.setTaxID(payers.getValue2());
        payerVO.setContactInfo(payers.getValue3());

        return  ResultVo.success(payerVO);

    }

    /**
     * 更改信息
     * @param payerVO
     * @return
     */
    @PostMapping("/modifyPayer")
    public ResultVo modifyPayers(@RequestBody PayerVO payerVO){
        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoSuiteOfPayer);

        TransactionReceipt transactionReceipt = taxContract.ModifyPayers(payerVO.getName(), payerVO.getTaxID(), payerVO.getContactInfo());
        if (transactionReceipt.getStatus().equals("0x0")){
            return  ResultVo.success(transactionReceipt);
        }else {
            return ResultVo.fail(transactionReceipt.getStatusMsg());
        }
    }
    /**
     * 返回纳税人用户地址信息
     */
    @GetMapping("/getAccount")
    public ResultVo getAccount(){
        return ResultVo.success(cryptoSuiteOfPayer.getAddress());
    }

    /**
     * 返回税务端用户信息
     */
    @GetMapping("/getTaxation")
    public ResultVo getTaxation(){
        return ResultVo.success(cryptoSuiteOfTation.getAddress());
    }

    /////////////发票管理模块////////////////


    /**
     * 创建发票
     * @param invoiceVO
     * @return
     */
    @PostMapping("/createInvoice")
    public ResultVo createInvoice(@RequestBody InvoiceVO invoiceVO){
        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoSuiteOfPayer);
        BigInteger bigInteger = Utils.datetimeToTimestamp(invoiceVO.getCreationDate());
        TransactionReceipt transactionReceipt = taxContract.createInvoice(
                invoiceVO.getCreator(),
                invoiceVO.getRecipient(),
                invoiceVO.getTotalAmount(),
                invoiceVO.getDescription(),
                bigInteger,
                invoiceVO.getAuditInfo());
        /**
         * 创建成功 返回发票id
         */
        if (transactionReceipt.getStatus().equals("0x0")){
            return  ResultVo.success(transactionReceipt.getOutput());
        }else {
            return ResultVo.fail(transactionReceipt.getStatusMsg());
        }

    }

    /**
     * 更改发票
     * @param InvoiceId
     * @param invoiceVO
     * @return
     */
    @PostMapping("/modifyInvoice")
    public ResultVo modifyInvoice(@RequestParam(value = "InvoiceId") String InvoiceId,
                                  @RequestBody InvoiceVO invoiceVO){

        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoSuiteOfPayer);
        byte[] bytes = Utils.convertBytes32StringToByteArray(InvoiceId);
        BigInteger bigInteger = Utils.datetimeToTimestamp(invoiceVO.getCreationDate());
        TransactionReceipt transactionReceipt = taxContract.modifyInvoice(bytes, invoiceVO.getTotalAmount(), invoiceVO.getDescription(), bigInteger);

//      TransactionDecoderInterface transactionDecoderService = new TransactionDecoderService(client.getCryptoSuite());
//        try {
//
//            String string = transactionResponse.getReturnObject().get(0).toString();
//
//
//        } catch (TransactionException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (ABICodecException e) {
//            throw new RuntimeException(e);
//        }
        if (transactionReceipt.getStatus().equals("0x0")){
            return  ResultVo.success(transactionReceipt);
        }else {
            return ResultVo.fail(transactionReceipt.getStatusMsg());
        }

    }

    /**
     * 单一发票查询
     * @param InvoiceId
     * @return
     */
    @GetMapping("/getInvoice")
    public ResultVo getInvoice(@RequestParam(value = "InvoiceId") String InvoiceId){
        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoSuiteOfPayer);
        byte[] bytes = Utils.convertBytes32StringToByteArray(InvoiceId);
        try {
            Invoice.InvoiceInfo invoice = taxContract.getInvoice(bytes);
            InvoiceVO invoiceVO = new InvoiceVO();

            invoiceVO.setCreator(invoice.creator);
            invoiceVO.setRecipient(invoice.recipient);
            invoiceVO.setTotalAmount(invoice.totalAmount);
            invoiceVO.setCreationDate(Utils.timestampDatetime(invoice.creationDate));
            invoiceVO.setDescription(invoice.description);
            invoiceVO.setStatus(invoice.status);
            invoiceVO.setCreator(invoice.creator);
            return ResultVo.success(invoiceVO);
        } catch (ContractException e) {
            throw new RuntimeException(e);
        }

    }


///////////////////////////报销模块//////////////////////////////////////

    /**
     * 提交报销
     * @param InvoiceId
     * @return
     */
    @PostMapping("/reimbursementRequests")
    public ResultVo ReimbursementRequests(@RequestParam(value = "InvoiceId") String InvoiceId){
        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoSuiteOfPayer);
        byte[] bytes = Utils.convertBytes32StringToByteArray(InvoiceId);
        TransactionReceipt transactionReceipt = taxContract.ReimbursementRequests(bytes);

        if (transactionReceipt.getStatus().equals("0x0")){
            return  ResultVo.success(transactionReceipt);
        }else {
            return ResultVo.fail(transactionReceipt.getStatusMsg());
        }
    }

    /**
     * 处理报销
     * @param InvoiceId
     * @return
     */
    @PostMapping("/dealInvoice")
    public ResultVo DealInvoice(@RequestParam(value = "InvoiceId") String InvoiceId){
        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoSuiteOfTation);
        byte[] bytes = Utils.convertBytes32StringToByteArray(InvoiceId);
        TransactionReceipt transactionReceipt = taxContract.DealInvoice(bytes);

        //TransactionDecoderInterface transactionDecoderService = new TransactionDecoderService(client.getCryptoSuite());

//        TransactionDecoderInterface transactionDecoderService = new TransactionDecoderService(client.getCryptoSuite());
//        try {
//            TransactionResponse transactionResponse = transactionDecoderService.decodeReceiptWithValues(TaxContract.ABI, TaxContract.FUNC_DEALINVOICE, transactionReceipt);
//            String string = transactionResponse.getReturnObject().get(0).toString();
//
//
//        } catch (TransactionException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (ABICodecException e) {
//            throw new RuntimeException(e);
//        }

        if (transactionReceipt.getStatus().equals("0x0")){
            return  ResultVo.success(transactionReceipt);
        }else {
            return ResultVo.fail(transactionReceipt.getStatusMsg());
        }
    }
    /**
     * 审批报销
     * @param InvoiceId
     * @param invoiceVO
     * @return
     */
    @PostMapping("/approvalReimbursement")
    public ResultVo ApprovalReimbursement(@RequestParam(value = "InvoiceId") String InvoiceId,
                                          @RequestBody InvoiceVO invoiceVO){
        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoSuiteOfPayer);
        byte[] bytes = Utils.convertBytes32StringToByteArray(InvoiceId);
        TransactionReceipt transactionReceipt = taxContract.ApprovalReimbursement(bytes, invoiceVO.getTotalAmount());
        if (transactionReceipt.getStatus().equals("0x0")){
            return  ResultVo.success(transactionReceipt);
        }else {
            return ResultVo.fail(transactionReceipt.getStatusMsg());
        }

    }
    /**
     * 查询所有发票
     */
    @GetMapping("/getAllInvoice")
    public ResultVo GetAllInvoice(){
        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoSuiteOfPayer);
        ArrayList<InvoiceVO> invoiceVOArrayList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            try {
                byte[] bytes = taxContract.InvoiceIds(cryptoSuiteOfPayer.getAddress(), BigInteger.valueOf(i));
                String s = Utils.bytesToHex(bytes);
                Invoice.InvoiceInfo invoice = taxContract.getInvoice(bytes);

                    InvoiceVO invoiceVO = new InvoiceVO();

                    invoiceVO.setCreator(invoice.creator);
                    invoiceVO.setRecipient(invoice.recipient);
                    invoiceVO.setTotalAmount(invoice.totalAmount);
                    invoiceVO.setCreationDate(Utils.timestampDatetime(invoice.creationDate));
                    invoiceVO.setDescription(invoice.description);
                    invoiceVO.setStatus(invoice.status);
                    invoiceVO.setCreator(invoice.creator);
                    invoiceVOArrayList.add(invoiceVO);


            } catch (ContractException e) {
                    return  ResultVo.success(invoiceVOArrayList);
            }
        }
        return  ResultVo.success(invoiceVOArrayList);
    }
    /**
     * 查询发票状态
     *
     */

    @GetMapping("/getInvoiceStatus")
    public ResultVo GetInvoiceStatus(@RequestParam(value = "InvoiceId") String InvoiceId) throws ContractException {
        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoSuiteOfPayer);
        byte[] bytes = Utils.convertBytes32StringToByteArray(InvoiceId);
        BigInteger invoiceStatus = taxContract.getInvoiceStatus(bytes);
        return ResultVo.success(invoiceStatus);
    }
    /**
     * 查询是否请求报销
     */
    @GetMapping("/getIsRequest")
    public ResultVo GetIsRequest(@RequestParam(value = "InvoiceId") String InvoiceId) throws ContractException {
        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoSuiteOfPayer);
        byte[] bytes = Utils.convertBytes32StringToByteArray(InvoiceId);
        Boolean request = taxContract.isRequest(bytes);
        return ResultVo.success(request);
    }
    /**
     * 查询所有Id数组
     */
    @GetMapping("/getAllInvoiceIds")
    public ResultVo GetAllInvoiceIds(){
        TaxContract taxContract = TaxContract.load(ContractAddress, client, cryptoSuiteOfPayer);
        //List invoiceIdArr = taxContract.getInvoiceIdArr();
        //TODO
        ArrayList<InvoiceStatusVO> arrayList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            InvoiceStatusVO invoiceStatusVO = new InvoiceStatusVO();

            try {
                byte[]  bytes = taxContract.InvoiceIds(cryptoSuiteOfPayer.getAddress(), BigInteger.valueOf(i));
                String string = Utils.bytesToHex(bytes);
                BigInteger status = taxContract.getInvoiceStatus(bytes);
                invoiceStatusVO.setInvoiceID(string);
                invoiceStatusVO.setStatus(status);
                arrayList.add(invoiceStatusVO);
            } catch (ContractException e) {
                return ResultVo.success(arrayList);
            }

        }


        return ResultVo.success(arrayList);
    }
}
