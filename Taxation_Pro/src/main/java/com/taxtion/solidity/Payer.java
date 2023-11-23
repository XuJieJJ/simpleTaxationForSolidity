package com.taxtion.solidity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionEncoder;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class Payer extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040523480156200001157600080fd5b5060405162000d9338038062000d938339810160408190526200003491620001dd565b600080546001600160a01b0319163317905582516200005b90600290602086019062000091565b5081516200007190600390602085019062000091565b5080516200008790600490602084019062000091565b505050506200026a565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620000d457805160ff191683800117855562000104565b8280016001018555821562000104579182015b8281111562000104578251825591602001919060010190620000e7565b506200011292915062000116565b5090565b6200013391905b808211156200011257600081556001016200011d565b90565b600082601f83011262000147578081fd5b81516001600160401b03808211156200015e578283fd5b6040516020601f8401601f191682018101838111838210171562000180578586fd5b806040525081945083825286818588010111156200019d57600080fd5b600092505b83831015620001c15785830181015182840182015291820191620001a2565b83831115620001d35760008185840101525b5050505092915050565b600080600060608486031215620001f2578283fd5b83516001600160401b038082111562000209578485fd5b620002178783880162000136565b945060208601519150808211156200022d578384fd5b6200023b8783880162000136565b9350604086015191508082111562000251578283fd5b50620002608682870162000136565b9150509250925092565b610b19806200027a6000396000f3fe608060405234801561001057600080fd5b50600436106100885760003560e01c80635a9b0b891161005b5780635a9b0b89146100df5780639fc0f2ad146100e7578063b76af158146100fa578063f41b9c691461010f57610088565b80630a27d2891461008d57806310e9e2c0146100ad57806322366844146100c257806346f4a17f146100d7575b600080fd5b610095610124565b6040516100a493929190610a1b565b60405180910390f35b6100c06100bb3660046108e7565b6102e3565b005b6100ca6103c0565b6040516100a491906109e2565b6100c06103d9565b6100956105b8565b6100c06100f536600461096b565b610779565b61010261077c565b6040516100a49190610ada565b610117610782565b6040516100a491906109ce565b60028054604080516020601f600019610100600187161502019094168590049384018190048102820181019092528281529183918301828280156101a95780601f1061017e576101008083540402835291602001916101a9565b820191906000526020600020905b81548152906001019060200180831161018c57829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102475780601f1061021c57610100808354040283529160200191610247565b820191906000526020600020905b81548152906001019060200180831161022a57829003601f168201915b50505060028085018054604080516020601f60001961010060018716150201909416959095049283018590048502810185019091528181529596959450909250908301828280156102d95780601f106102ae576101008083540402835291602001916102d9565b820191906000526020600020905b8154815290600101906020018083116102bc57829003601f168201915b5050505050905083565b6000546001600160a01b031633146103165760405162461bcd60e51b815260040161030d90610a5d565b60405180910390fd5b61031e6103c0565b61033a5760405162461bcd60e51b815260040161030d90610aa3565b825161034d906002906020860190610791565b508151610361906003906020850190610791565b508051610375906004906020840190610791565b50336001600160a01b03167f99ea912120f5b9c77645c0f7a2806f4f49902b9e1a6127b7e2c1041e47538c5a8484846040516103b393929190610a1b565b60405180910390a2505050565b6002805460001961010060018316150201160415155b90565b6000546001600160a01b031633146104035760405162461bcd60e51b815260040161030d90610a5d565b61040b6103c0565b6104275760405162461bcd60e51b815260040161030d90610aa3565b6002805460408051602060018416156101000260001901909316849004601f81018490048402820184019092528181523393606093919290918301828280156104b15780601f10610486576101008083540402835291602001916104b1565b820191906000526020600020905b81548152906001019060200180831161049457829003601f168201915b505060038054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152969750606096955091935091508301828280156105455780601f1061051a57610100808354040283529160200191610545565b820191906000526020600020905b81548152906001019060200180831161052857829003601f168201915b5050505050905060026000808201600061055f919061080f565b61056d60018301600061080f565b61057b60028301600061080f565b5050826001600160a01b03167f22bdda47343e54f728a9ea3470b21424c0b7deb72b573f52d10130c85a20f81083836040516103b39291906109ed565b60028054604080516020601f60001961010060018716150201909416859004938401819004810282018101909252828152606093849384939192600392600492859183018282801561064b5780601f106106205761010080835404028352916020019161064b565b820191906000526020600020905b81548152906001019060200180831161062e57829003601f168201915b5050855460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152959850879450925084019050828280156106d95780601f106106ae576101008083540402835291602001916106d9565b820191906000526020600020905b8154815290600101906020018083116106bc57829003601f168201915b5050845460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152959750869450925084019050828280156107675780601f1061073c57610100808354040283529160200191610767565b820191906000526020600020905b81548152906001019060200180831161074a57829003601f168201915b50505050509050925092509250909192565b50565b60015481565b6000546001600160a01b031681565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106107d257805160ff19168380011785556107ff565b828001600101855582156107ff579182015b828111156107ff5782518255916020019190600101906107e4565b5061080b92915061084f565b5090565b50805460018160011615610100020316600290046000825580601f106108355750610779565b601f01602090049060005260206000209081019061077991905b6103d691905b8082111561080b5760008155600101610855565b600082601f830112610879578081fd5b813567ffffffffffffffff80821115610890578283fd5b604051601f8301601f1916810160200182811182821017156108b0578485fd5b6040528281529250828483016020018610156108cb57600080fd5b8260208601602083013760006020848301015250505092915050565b6000806000606084860312156108fb578283fd5b833567ffffffffffffffff80821115610912578485fd5b61091e87838801610869565b94506020860135915080821115610933578384fd5b61093f87838801610869565b93506040860135915080821115610954578283fd5b5061096186828701610869565b9150509250925092565b60006020828403121561097c578081fd5b5035919050565b60008151808452815b818110156109a85760208185018101518683018201520161098c565b818111156109b95782602083870101525b50601f01601f19169290920160200192915050565b6001600160a01b0391909116815260200190565b901515815260200190565b600060408252610a006040830185610983565b8281036020840152610a128185610983565b95945050505050565b600060608252610a2e6060830186610983565b8281036020840152610a408186610983565b8381036040850152610a528186610983565b979650505050505050565b60208082526026908201527f4f6e6c7920746865206f776e65722063616e2063616c6c20746869732066756e60408201526531ba34b7b71760d11b606082015260800190565b60208082526017908201527f5461787061796572206e6f742072656769737465722121000000000000000000604082015260600190565b9081526020019056fea26469706673582212204474aa17a014caef14a32dc9033ec7d1c0fd606bac5eada807962bc81023ec2464736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"60806040523480156200001157600080fd5b5060405162000d9b38038062000d9b8339810160408190526200003491620001dd565b600080546001600160a01b0319163317905582516200005b90600290602086019062000091565b5081516200007190600390602085019062000091565b5080516200008790600490602084019062000091565b505050506200026a565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620000d457805160ff191683800117855562000104565b8280016001018555821562000104579182015b8281111562000104578251825591602001919060010190620000e7565b506200011292915062000116565b5090565b6200013391905b808211156200011257600081556001016200011d565b90565b600082601f83011262000147578081fd5b81516001600160401b03808211156200015e578283fd5b6040516020601f8401601f191682018101838111838210171562000180578586fd5b806040525081945083825286818588010111156200019d57600080fd5b600092505b83831015620001c15785830181015182840182015291820191620001a2565b83831115620001d35760008185840101525b5050505092915050565b600080600060608486031215620001f2578283fd5b83516001600160401b038082111562000209578485fd5b620002178783880162000136565b945060208601519150808211156200022d578384fd5b6200023b8783880162000136565b9350604086015191508082111562000251578283fd5b50620002608682870162000136565b9150509250925092565b610b21806200027a6000396000f3fe608060405234801561001057600080fd5b50600436106100885760003560e01c8063cf9ce6da1161005b578063cf9ce6da146100e1578063d844aafb146100e9578063def8595a146100fc578063dfd873101461010f57610088565b80632809de251461008d57806342a850ba146100ab57806390dd49bd146100c2578063b17b952f146100d7575b600080fd5b610095610124565b6040516100a291906109ea565b60405180910390f35b6100b361013d565b6040516100a293929190610a23565b6100ca6102fc565b6040516100a291906109d6565b6100df61030b565b005b6100b3610502565b6100df6100f73660046108ef565b6106c3565b6100df61010a366004610973565b61078c565b61011761078f565b6040516100a29190610ae2565b6002805460001961010060018316150201160415155b90565b60028054604080516020601f600019610100600187161502019094168590049384018190048102820181019092528281529183918301828280156101c25780601f10610197576101008083540402835291602001916101c2565b820191906000526020600020905b8154815290600101906020018083116101a557829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102605780601f1061023557610100808354040283529160200191610260565b820191906000526020600020905b81548152906001019060200180831161024357829003601f168201915b50505060028085018054604080516020601f60001961010060018716150201909416959095049283018590048502810185019091528181529596959450909250908301828280156102f25780601f106102c7576101008083540402835291602001916102f2565b820191906000526020600020905b8154815290600101906020018083116102d557829003601f168201915b5050505050905083565b6000546001600160a01b031681565b6000546001600160a01b0316331461033f57604051636381e58960e11b815260040161033690610a65565b60405180910390fd5b610347610124565b61036457604051636381e58960e11b815260040161033690610aab565b6002805460408051602060018416156101000260001901909316849004601f81018490048402820184019092528181523393606093919290918301828280156103ee5780601f106103c3576101008083540402835291602001916103ee565b820191906000526020600020905b8154815290600101906020018083116103d157829003601f168201915b505060038054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152969750606096955091935091508301828280156104825780601f1061045757610100808354040283529160200191610482565b820191906000526020600020905b81548152906001019060200180831161046557829003601f168201915b5050505050905060026000808201600061049c9190610795565b6104aa600183016000610795565b6104b8600283016000610795565b5050826001600160a01b03167f5345bd628609da8de06e405ca11b485e3dc768a4a76ece41d5252974db59a80283836040516104f59291906109f5565b60405180910390a2505050565b60028054604080516020601f6000196101006001871615020190941685900493840181900481028201810190925282815260609384938493919260039260049285918301828280156105955780601f1061056a57610100808354040283529160200191610595565b820191906000526020600020905b81548152906001019060200180831161057857829003601f168201915b5050855460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152959850879450925084019050828280156106235780601f106105f857610100808354040283529160200191610623565b820191906000526020600020905b81548152906001019060200180831161060657829003601f168201915b5050845460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152959750869450925084019050828280156106b15780601f10610686576101008083540402835291602001916106b1565b820191906000526020600020905b81548152906001019060200180831161069457829003601f168201915b50505050509050925092509250909192565b6000546001600160a01b031633146106ee57604051636381e58960e11b815260040161033690610a65565b6106f6610124565b61071357604051636381e58960e11b815260040161033690610aab565b82516107269060029060208601906107d9565b50815161073a9060039060208501906107d9565b50805161074e9060049060208401906107d9565b50336001600160a01b03167fcec8bedd530328dac7b099df364fbcb958f951cca7ab3482be475f56fe512a368484846040516104f593929190610a23565b50565b60015481565b50805460018160011615610100020316600290046000825580601f106107bb575061078c565b601f01602090049060005260206000209081019061078c9190610857565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061081a57805160ff1916838001178555610847565b82800160010185558215610847579182015b8281111561084757825182559160200191906001019061082c565b50610853929150610857565b5090565b61013a91905b80821115610853576000815560010161085d565b600082601f830112610881578081fd5b813567ffffffffffffffff80821115610898578283fd5b604051601f8301601f1916810160200182811182821017156108b8578485fd5b6040528281529250828483016020018610156108d357600080fd5b8260208601602083013760006020848301015250505092915050565b600080600060608486031215610903578283fd5b833567ffffffffffffffff8082111561091a578485fd5b61092687838801610871565b9450602086013591508082111561093b578384fd5b61094787838801610871565b9350604086013591508082111561095c578283fd5b5061096986828701610871565b9150509250925092565b600060208284031215610984578081fd5b5035919050565b60008151808452815b818110156109b057602081850181015186830182015201610994565b818111156109c15782602083870101525b50601f01601f19169290920160200192915050565b6001600160a01b0391909116815260200190565b901515815260200190565b600060408252610a08604083018561098b565b8281036020840152610a1a818561098b565b95945050505050565b600060608252610a36606083018661098b565b8281036020840152610a48818661098b565b8381036040850152610a5a818661098b565b979650505050505050565b60208082526026908201527f4f6e6c7920746865206f776e65722063616e2063616c6c20746869732066756e60408201526531ba34b7b71760d11b606082015260800190565b60208082526017908201527f5461787061796572206e6f742072656769737465722121000000000000000000604082015260600190565b9081526020019056fea26469706673582212205b20bbb07afd0cfa965db42e1efb77db741193bf40ac9ad16d777a8dccf203d564736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"string\",\"name\":\"_name\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"_taxID\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"_contactInfo\",\"type\":\"string\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"taxpayer\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"taxID\",\"type\":\"string\"}],\"name\":\"TaxpayerRegistered\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"addr\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"taxID\",\"type\":\"string\"}],\"name\":\"TaxpayerRemove\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"taxpayer\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"taxID\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"contactInfo\",\"type\":\"string\"}],\"name\":\"TaxpayerUpdated\",\"type\":\"event\"},{\"inputs\":[],\"name\":\"ReimbursedAmount\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"Reimbursement\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getInfo\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"isRegistered\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"ownerPayer\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"removeTaxpayer\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"taxpayerInfo\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"taxID\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"contactInfo\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"taxID\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"contactInfo\",\"type\":\"string\"}],\"name\":\"updateTaxpayerInfo\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_REIMBURSEDAMOUNT = "ReimbursedAmount";

    public static final String FUNC_REIMBURSEMENT = "Reimbursement";

    public static final String FUNC_GETINFO = "getInfo";

    public static final String FUNC_ISREGISTERED = "isRegistered";

    public static final String FUNC_OWNERPAYER = "ownerPayer";

    public static final String FUNC_REMOVETAXPAYER = "removeTaxpayer";

    public static final String FUNC_TAXPAYERINFO = "taxpayerInfo";

    public static final String FUNC_UPDATETAXPAYERINFO = "updateTaxpayerInfo";

    public static final Event TAXPAYERREGISTERED_EVENT = new Event("TaxpayerRegistered",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event TAXPAYERREMOVE_EVENT = new Event("TaxpayerRemove",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event TAXPAYERUPDATED_EVENT = new Event("TaxpayerUpdated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected Payer(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public List<TaxpayerRegisteredEventResponse> getTaxpayerRegisteredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TAXPAYERREGISTERED_EVENT, transactionReceipt);
        ArrayList<TaxpayerRegisteredEventResponse> responses = new ArrayList<TaxpayerRegisteredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TaxpayerRegisteredEventResponse typedResponse = new TaxpayerRegisteredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.taxpayer = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.taxID = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeTaxpayerRegisteredEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(TAXPAYERREGISTERED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeTaxpayerRegisteredEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(TAXPAYERREGISTERED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<TaxpayerRemoveEventResponse> getTaxpayerRemoveEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TAXPAYERREMOVE_EVENT, transactionReceipt);
        ArrayList<TaxpayerRemoveEventResponse> responses = new ArrayList<TaxpayerRemoveEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TaxpayerRemoveEventResponse typedResponse = new TaxpayerRemoveEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.addr = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.taxID = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeTaxpayerRemoveEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(TAXPAYERREMOVE_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeTaxpayerRemoveEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(TAXPAYERREMOVE_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<TaxpayerUpdatedEventResponse> getTaxpayerUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TAXPAYERUPDATED_EVENT, transactionReceipt);
        ArrayList<TaxpayerUpdatedEventResponse> responses = new ArrayList<TaxpayerUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TaxpayerUpdatedEventResponse typedResponse = new TaxpayerUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.taxpayer = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.taxID = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.contactInfo = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeTaxpayerUpdatedEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(TAXPAYERUPDATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeTaxpayerUpdatedEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(TAXPAYERUPDATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public BigInteger ReimbursedAmount() throws ContractException {
        final Function function = new Function(FUNC_REIMBURSEDAMOUNT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt Reimbursement(BigInteger amount) {
        final Function function = new Function(
                FUNC_REIMBURSEMENT,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] Reimbursement(BigInteger amount, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REIMBURSEMENT,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForReimbursement(BigInteger amount) {
        final Function function = new Function(
                FUNC_REIMBURSEMENT,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<BigInteger> getReimbursementInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REIMBURSEMENT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
        );
    }

    public Tuple3<String, String, String> getInfo() throws ContractException {
        final Function function = new Function(FUNC_GETINFO,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple3<String, String, String>(
                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue());
    }

    public Boolean isRegistered() throws ContractException {
        final Function function = new Function(FUNC_ISREGISTERED,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public String ownerPayer() throws ContractException {
        final Function function = new Function(FUNC_OWNERPAYER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public TransactionReceipt removeTaxpayer() {
        final Function function = new Function(
                FUNC_REMOVETAXPAYER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] removeTaxpayer(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REMOVETAXPAYER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRemoveTaxpayer() {
        final Function function = new Function(
                FUNC_REMOVETAXPAYER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple3<String, String, String> taxpayerInfo() throws ContractException {
        final Function function = new Function(FUNC_TAXPAYERINFO,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple3<String, String, String>(
                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue());
    }

    public TransactionReceipt updateTaxpayerInfo(String name, String taxID, String contactInfo) {
        final Function function = new Function(
                FUNC_UPDATETAXPAYERINFO,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(name),
                        new org.fisco.bcos.sdk.abi.datatypes.Utf8String(taxID),
                        new org.fisco.bcos.sdk.abi.datatypes.Utf8String(contactInfo)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] updateTaxpayerInfo(String name, String taxID, String contactInfo, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_UPDATETAXPAYERINFO,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(name),
                        new org.fisco.bcos.sdk.abi.datatypes.Utf8String(taxID),
                        new org.fisco.bcos.sdk.abi.datatypes.Utf8String(contactInfo)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForUpdateTaxpayerInfo(String name, String taxID, String contactInfo) {
        final Function function = new Function(
                FUNC_UPDATETAXPAYERINFO,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(name),
                        new org.fisco.bcos.sdk.abi.datatypes.Utf8String(taxID),
                        new org.fisco.bcos.sdk.abi.datatypes.Utf8String(contactInfo)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple3<String, String, String> getUpdateTaxpayerInfoInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_UPDATETAXPAYERINFO,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<String, String, String>(

                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue()
        );
    }

    public static Payer load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Payer(contractAddress, client, credential);
    }

    public static Payer deploy(Client client, CryptoKeyPair credential, String _name, String _taxID, String _contactInfo) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_name),
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_taxID),
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_contactInfo)));
        return deploy(Payer.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }

    public static class TaxpayerRegisteredEventResponse {
        public TransactionReceipt.Logs log;

        public String taxpayer;

        public String name;

        public String taxID;
    }

    public static class TaxpayerRemoveEventResponse {
        public TransactionReceipt.Logs log;

        public String addr;

        public String name;

        public String taxID;
    }

    public static class TaxpayerUpdatedEventResponse {
        public TransactionReceipt.Logs log;

        public String taxpayer;

        public String name;

        public String taxID;

        public String contactInfo;
    }
}