package com.taxtion.controller;

import com.taxtion.vo.ResultVo;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BcosBlock;
import org.fisco.bcos.sdk.client.protocol.response.TotalTransactionCount;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.*;

@RestController
@RequestMapping("/blockchain")
public class BlockChainController {


    @Autowired
    private Client client;

    /**
     * 获取最新十个区块高度的信息
     * @return
     */
    @GetMapping("/getBlock")
    public ResultVo getBlockChainInfo(){

        TotalTransactionCount totalTransactionCount = client.getTotalTransactionCount();
        BigInteger blockNumber = client.getBlockNumber().getBlockNumber();

        List<BcosBlock.Block> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BigInteger block = blockNumber.subtract(BigInteger.valueOf(i));
            if (block.intValue() > 0){
                BcosBlock blockByNumber = client.getBlockByNumber(block, true);
                data.add(blockByNumber.getBlock());
            }

        }

        return ResultVo.success(data);
    }




}
