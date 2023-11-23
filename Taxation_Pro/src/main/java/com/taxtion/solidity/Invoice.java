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
import org.fisco.bcos.sdk.abi.datatypes.DynamicStruct;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple8;
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
public class Invoice extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040523480156200001157600080fd5b5060405162000e0438038062000e048339810160408190526200003491620002d4565b81831115620000605760405162461bcd60e51b81526004016200005790620003a3565b60405180910390fd5b600080546001600160a01b03191633179055620000856001600160e01b036200015616565b600155600280546001600160a01b03808a166001600160a01b031992831617909255600380549289169290911691909117905560048590558351620000d290600590602087019062000193565b50600683905560078290558051620000f290600990602084019062000193565b50856001600160a01b0316876001600160a01b03166001547f9a4183fd6c7487f5c95cd540778dd2ec6ab302f45f612fd7436e009f700df9a888888888604051620001419493929190620003c7565b60405180910390a45050505050505062000445565b60008042336001430340604051602001620001749392919062000380565b60408051601f1981840301815291905280516020909101209150505b90565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620001d657805160ff191683800117855562000206565b8280016001018555821562000206579182015b8281111562000206578251825591602001919060010190620001e9565b506200021492915062000218565b5090565b6200019091905b808211156200021457600081556001016200021f565b80516001600160a01b03811681146200024d57600080fd5b92915050565b600082601f83011262000264578081fd5b81516001600160401b03808211156200027b578283fd5b604051601f8301601f1916810160200182811182821017156200029c578485fd5b604052828152925082848301602001861015620002b857600080fd5b620002cb83602083016020880162000412565b50505092915050565b600080600080600080600060e0888a031215620002ef578283fd5b620002fb898962000235565b96506200030c8960208a0162000235565b604089015160608a015191975095506001600160401b038082111562000330578485fd5b6200033e8b838c0162000253565b955060808a0151945060a08a0151935060c08a015191508082111562000362578283fd5b50620003718a828b0162000253565b91505092959891949750929550565b92835260609190911b6001600160601b0319166020830152603482015260540190565b6020808252600a90820152696461746520776f726e6760b01b604082015260600190565b6000858252608060208301528451806080840152620003ee8160a085016020890162000412565b604083019490945250606081019190915260a0601f909201601f1916010192915050565b60005b838110156200042f57818101518382015260200162000415565b838111156200043f576000848401525b50505050565b6109af80620004556000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c806370c862861161006657806370c862861461010f5780637cd87ba2146101175780638da5cb5b1461011f578063cdee130414610134578063e2f8b619146101475761009e565b8063039f3b1e146100a357806308109884146100b85780633263e78b146100d657806339465c02146100de5780634e69d560146100fa575b600080fd5b6100b66100b136600461066a565b61015c565b005b6100c0610228565b6040516100cd9190610807565b60405180910390f35b6100c061022f565b6100e6610235565b6040516100cd989796959493929190610797565b610102610388565b6040516100cd9190610810565b6100c0610391565b6100c0610397565b61012761039d565b6040516100cd9190610783565b6100b6610142366004610644565b6103ac565b61014f6103cd565b6040516100cd91906108a2565b6000546001600160a01b0316331461018f5760405162461bcd60e51b815260040161018690610825565b60405180910390fd5b6000610199610388565b60028111156101a457fe5b146101c15760405162461bcd60e51b81526004016101869061086b565b600484905582516101d9906005906020860190610568565b50600682905560078190556040517f9f636637cc40f5478b2ef6a6eb8c172d9d1231999e42c60b41c5d1e3808768c49061021a90869086908690869061093d565b60405180910390a150505050565b6007545b90565b60015490565b600280546003546004546005805460408051602061010060018516150260001901909316889004601f81018490048402820184019092528181526001600160a01b03968716979690951695939492918301828280156102d55780601f106102aa576101008083540402835291602001916102d5565b820191906000526020600020905b8154815290600101906020018083116102b857829003601f168201915b505050600484015460058501546006860154600787018054604080516020601f600260001960018716156101000201909516949094049384018190048102820181019092528281529899959894975060ff9093169550909183018282801561037e5780601f106103535761010080835404028352916020019161037e565b820191906000526020600020905b81548152906001019060200180831161036157829003601f168201915b5050505050905088565b60085460ff1690565b60015481565b60065490565b6000546001600160a01b031681565b6008805482919060ff191660018360028111156103c557fe5b021790555050565b6103d56105e6565b604080516101008082018352600280546001600160a01b039081168452600354166020808501919091526004548486015260058054865160018216159095026000190116839004601f810183900483028501830190965285845293949193606086019392909183018282801561048c5780601f106104615761010080835404028352916020019161048c565b820191906000526020600020905b81548152906001019060200180831161046f57829003601f168201915b50505091835250506004820154602082015260058201546040820152600682015460609091019060ff1660028111156104c157fe5b60028111156104cc57fe5b815260078201805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815293820193929183018282801561055a5780601f1061052f5761010080835404028352916020019161055a565b820191906000526020600020905b81548152906001019060200180831161053d57829003601f168201915b505050505081525050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105a957805160ff19168380011785556105d6565b828001600101855582156105d6579182015b828111156105d65782518255916020019190600101906105bb565b506105e292915061062a565b5090565b60408051610100810182526000808252602082018190529181018290526060808201526080810182905260a081018290529060c08201908152602001606081525090565b61022c91905b808211156105e25760008155600101610630565b600060208284031215610655578081fd5b813560038110610663578182fd5b9392505050565b6000806000806080858703121561067f578283fd5b8435935060208086013567ffffffffffffffff8082111561069e578586fd5b81880189601f8201126106af578687fd5b80359250818311156106bf578687fd5b604051601f8401601f19168101850183811182821017156106de578889fd5b60405283815281840185018b10156106f4578788fd5b838583018683013792830190930195909552949794965050505060408301359260600135919050565b6001600160a01b03169052565b6003811061073457fe5b9052565b60008151808452815b8181101561075d57602081850181015186830182015201610741565b8181111561076e5782602083870101525b50601f01601f19169290920160200192915050565b6001600160a01b0391909116815260200190565b6001600160a01b0389811682528816602082015260408101879052610100606082018190526000906107cb83820189610738565b8760808501528660a08501526107e086610969565b60c085015283810360e08501526107f78186610738565b9c9b505050505050505050505050565b90815260200190565b6020810161081d83610969565b825292915050565b60208082526026908201527f4f6e6c7920746865206f776e65722063616e2063616c6c20746869732066756e60408201526531ba34b7b71760d11b606082015260800190565b60208082526019908201527f696e766f69636520686173206265656e20617070726f76656400000000000000604082015260600190565b6000602082526108b660208301845161071d565b60208301516108c8604084018261071d565b506040830151606083015260608301516101008060808501526108ef610120850183610738565b608086015160a086015260a086015160c086015260c0860151925061091760e086018461072a565b60e0860151858203601f19018387015292506109338184610738565b9695505050505050565b6000858252608060208301526109566080830186610738565b6040830194909452506060015292915050565b806003811061097457fe5b91905056fea2646970667358221220f91a061062595896f5ac5d9caf59478d176ee916e793066125f1cacae619b62864736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"60806040523480156200001157600080fd5b5060405162000e0738038062000e078339810160408190526200003491620002d5565b818311156200006157604051636381e58960e11b81526004016200005890620003a4565b60405180910390fd5b600080546001600160a01b03191633179055620000866001600160e01b036200015716565b600155600280546001600160a01b03808a166001600160a01b031992831617909255600380549289169290911691909117905560048590558351620000d390600590602087019062000194565b50600683905560078290558051620000f390600990602084019062000194565b50856001600160a01b0316876001600160a01b03166001547fc08d8d19a2ef682d21517b744eb8581c62228fbf774a0658762bf7b9676285bd88888888604051620001429493929190620003c8565b60405180910390a45050505050505062000446565b60008042336001430340604051602001620001759392919062000381565b60408051601f1981840301815291905280516020909101209150505b90565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620001d757805160ff191683800117855562000207565b8280016001018555821562000207579182015b8281111562000207578251825591602001919060010190620001ea565b506200021592915062000219565b5090565b6200019191905b8082111562000215576000815560010162000220565b80516001600160a01b03811681146200024e57600080fd5b92915050565b600082601f83011262000265578081fd5b81516001600160401b03808211156200027c578283fd5b604051601f8301601f1916810160200182811182821017156200029d578485fd5b604052828152925082848301602001861015620002b957600080fd5b620002cc83602083016020880162000413565b50505092915050565b600080600080600080600060e0888a031215620002f0578283fd5b620002fc898962000236565b96506200030d8960208a0162000236565b604089015160608a015191975095506001600160401b038082111562000331578485fd5b6200033f8b838c0162000254565b955060808a0151945060a08a0151935060c08a015191508082111562000363578283fd5b50620003728a828b0162000254565b91505092959891949750929550565b92835260609190911b6001600160601b0319166020830152603482015260540190565b6020808252600a90820152696461746520776f726e6760b01b604082015260600190565b6000858252608060208301528451806080840152620003ef8160a085016020890162000413565b604083019490945250606081019190915260a0601f909201601f1916010192915050565b60005b838110156200043057818101518382015260200162000416565b8381111562000440576000848401525b50505050565b6109b180620004566000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c8063d7374a1b11610066578063d7374a1b14610115578063dc7d519b1461011d578063df21892114610130578063e6caa1031461014c578063f6b08830146101545761009e565b80630f583b98146100a357806338ec4fdd146100c15780635089e2c8146100d65780637f7b8fbc146100eb578063b7887c2514610100575b600080fd5b6100ab61015c565b6040516100b891906108a4565b60405180910390f35b6100d46100cf366004610646565b6102f8565b005b6100de610319565b6040516100b89190610785565b6100f3610328565b6040516100b89190610812565b610108610331565b6040516100b89190610809565b610108610337565b6100d461012b36600461066c565b61033d565b61013861040b565b6040516100b8989796959493929190610799565b61010861055e565b610108610564565b61016461056a565b604080516101008082018352600280546001600160a01b039081168452600354166020808501919091526004548486015260058054865160018216159095026000190116839004601f810183900483028501830190965285845293949193606086019392909183018282801561021b5780601f106101f05761010080835404028352916020019161021b565b820191906000526020600020905b8154815290600101906020018083116101fe57829003601f168201915b50505091835250506004820154602082015260058201546040820152600682015460609091019060ff16600281111561025057fe5b600281111561025b57fe5b815260078201805460408051602060026001851615610100026000190190941693909304601f81018490048402820184019092528181529382019392918301828280156102e95780601f106102be576101008083540402835291602001916102e9565b820191906000526020600020905b8154815290600101906020018083116102cc57829003601f168201915b50505050508152505090505b90565b6008805482919060ff1916600183600281111561031157fe5b021790555050565b6000546001600160a01b031681565b60085460ff1690565b60015490565b60065490565b6000546001600160a01b0316331461037157604051636381e58960e11b81526004016103689061085e565b60405180910390fd5b600061037b610328565b600281111561038657fe5b146103a457604051636381e58960e11b815260040161036890610827565b600484905582516103bc9060059060208601906105ae565b50600682905560078190556040517f16e8ec181abd841ac5c2f9dcd3d0b36eeac2d8420dc72994473f9091a713526a906103fd90869086908690869061093f565b60405180910390a150505050565b600280546003546004546005805460408051602061010060018516150260001901909316889004601f81018490048402820184019092528181526001600160a01b03968716979690951695939492918301828280156104ab5780601f10610480576101008083540402835291602001916104ab565b820191906000526020600020905b81548152906001019060200180831161048e57829003601f168201915b505050600484015460058501546006860154600787018054604080516020601f600260001960018716156101000201909516949094049384018190048102820181019092528281529899959894975060ff909316955090918301828280156105545780601f1061052957610100808354040283529160200191610554565b820191906000526020600020905b81548152906001019060200180831161053757829003601f168201915b5050505050905088565b60075490565b60015481565b60408051610100810182526000808252602082018190529181018290526060808201526080810182905260a081018290529060c08201908152602001606081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105ef57805160ff191683800117855561061c565b8280016001018555821561061c579182015b8281111561061c578251825591602001919060010190610601565b5061062892915061062c565b5090565b6102f591905b808211156106285760008155600101610632565b600060208284031215610657578081fd5b813560038110610665578182fd5b9392505050565b60008060008060808587031215610681578283fd5b8435935060208086013567ffffffffffffffff808211156106a0578586fd5b81880189601f8201126106b1578687fd5b80359250818311156106c1578687fd5b604051601f8401601f19168101850183811182821017156106e0578889fd5b60405283815281840185018b10156106f6578788fd5b838583018683013792830190930195909552949794965050505060408301359260600135919050565b6001600160a01b03169052565b6003811061073657fe5b9052565b60008151808452815b8181101561075f57602081850181015186830182015201610743565b818111156107705782602083870101525b50601f01601f19169290920160200192915050565b6001600160a01b0391909116815260200190565b6001600160a01b0389811682528816602082015260408101879052610100606082018190526000906107cd8382018961073a565b8760808501528660a08501526107e28661096b565b60c085015283810360e08501526107f9818661073a565b9c9b505050505050505050505050565b90815260200190565b6020810161081f8361096b565b825292915050565b60208082526019908201527f696e766f69636520686173206265656e20617070726f76656400000000000000604082015260600190565b60208082526026908201527f4f6e6c7920746865206f776e65722063616e2063616c6c20746869732066756e60408201526531ba34b7b71760d11b606082015260800190565b6000602082526108b860208301845161071f565b60208301516108ca604084018261071f565b506040830151606083015260608301516101008060808501526108f161012085018361073a565b608086015160a086015260a086015160c086015260c0860151925061091960e086018461072c565b60e0860151858203601f1901838701529250610935818461073a565b9695505050505050565b600085825260806020830152610958608083018661073a565b6040830194909452506060015292915050565b806003811061097657fe5b91905056fea26469706673582212205015d8b920b6496b27784d61be14c8a313d2277f78f310eb2695ac72f51c721e64736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"_creator\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"_recipient\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"_totalAmount\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"_description\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"_creationDate\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"_dueDate\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"_auditInfo\",\"type\":\"string\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"invoiceId\",\"type\":\"bytes32\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"creator\",\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"recipient\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"totalAmount\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"description\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"creationDate\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"dueDate\",\"type\":\"uint256\"}],\"name\":\"InvoiceCreated\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"totalAmount\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"description\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"creationDate\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"dueDate\",\"type\":\"uint256\"}],\"name\":\"InvoiceUpdate\",\"type\":\"event\"},{\"inputs\":[],\"name\":\"getCreateDate\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getDuedate\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getInvoice\",\"outputs\":[{\"components\":[{\"internalType\":\"address\",\"name\":\"creator\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"recipient\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"totalAmount\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"description\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"creationDate\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"dueDate\",\"type\":\"uint256\"},{\"internalType\":\"enum Invoice.InvoiceStatus\",\"name\":\"status\",\"type\":\"uint8\"},{\"internalType\":\"string\",\"name\":\"auditInfo\",\"type\":\"string\"}],\"internalType\":\"struct Invoice.InvoiceInfo\",\"name\":\"\",\"type\":\"tuple\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getInvoiceId\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getStatus\",\"outputs\":[{\"internalType\":\"enum Invoice.InvoiceStatus\",\"name\":\"\",\"type\":\"uint8\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"invoiceId\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"invoiceInfo\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"creator\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"recipient\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"totalAmount\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"description\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"creationDate\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"dueDate\",\"type\":\"uint256\"},{\"internalType\":\"enum Invoice.InvoiceStatus\",\"name\":\"status\",\"type\":\"uint8\"},{\"internalType\":\"string\",\"name\":\"auditInfo\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"_totalAmount\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"_description\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"_creationDate\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"_dueDate\",\"type\":\"uint256\"}],\"name\":\"modifyInvoice\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"enum Invoice.InvoiceStatus\",\"name\":\"invoiceStatus\",\"type\":\"uint8\"}],\"name\":\"modifyStatus\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_GETCREATEDATE = "getCreateDate";

    public static final String FUNC_GETDUEDATE = "getDuedate";

    public static final String FUNC_GETINVOICE = "getInvoice";

    public static final String FUNC_GETINVOICEID = "getInvoiceId";

    public static final String FUNC_GETSTATUS = "getStatus";

    public static final String FUNC_INVOICEID = "invoiceId";

    public static final String FUNC_INVOICEINFO = "invoiceInfo";

    public static final String FUNC_MODIFYINVOICE = "modifyInvoice";

    public static final String FUNC_MODIFYSTATUS = "modifyStatus";

    public static final String FUNC_OWNER = "owner";

    public static final Event INVOICECREATED_EVENT = new Event("InvoiceCreated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event INVOICEUPDATE_EVENT = new Event("InvoiceUpdate",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected Invoice(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public List<InvoiceCreatedEventResponse> getInvoiceCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(INVOICECREATED_EVENT, transactionReceipt);
        ArrayList<InvoiceCreatedEventResponse> responses = new ArrayList<InvoiceCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            InvoiceCreatedEventResponse typedResponse = new InvoiceCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.invoiceId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.creator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.recipient = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.totalAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.description = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.creationDate = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.dueDate = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeInvoiceCreatedEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(INVOICECREATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeInvoiceCreatedEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(INVOICECREATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<InvoiceUpdateEventResponse> getInvoiceUpdateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(INVOICEUPDATE_EVENT, transactionReceipt);
        ArrayList<InvoiceUpdateEventResponse> responses = new ArrayList<InvoiceUpdateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            InvoiceUpdateEventResponse typedResponse = new InvoiceUpdateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.totalAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.description = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.creationDate = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.dueDate = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeInvoiceUpdateEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(INVOICEUPDATE_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeInvoiceUpdateEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(INVOICEUPDATE_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public BigInteger getCreateDate() throws ContractException {
        final Function function = new Function(FUNC_GETCREATEDATE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger getDuedate() throws ContractException {
        final Function function = new Function(FUNC_GETDUEDATE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public Invoice.InvoiceInfo getInvoice() throws ContractException {
        final Function function = new Function(FUNC_GETINVOICE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Invoice.InvoiceInfo>() {}));
        return executeCallWithSingleValueReturn(function, Invoice.InvoiceInfo.class);
    }

    public byte[] getInvoiceId() throws ContractException {
        final Function function = new Function(FUNC_GETINVOICEID,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeCallWithSingleValueReturn(function, byte[].class);
    }

    public BigInteger getStatus() throws ContractException {
        final Function function = new Function(FUNC_GETSTATUS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public byte[] invoiceId() throws ContractException {
        final Function function = new Function(FUNC_INVOICEID,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeCallWithSingleValueReturn(function, byte[].class);
    }

    public Tuple8<String, String, BigInteger, String, BigInteger, BigInteger, BigInteger, String> invoiceInfo() throws ContractException {
        final Function function = new Function(FUNC_INVOICEINFO,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple8<String, String, BigInteger, String, BigInteger, BigInteger, BigInteger, String>(
                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (BigInteger) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue(),
                (BigInteger) results.get(5).getValue(),
                (BigInteger) results.get(6).getValue(),
                (String) results.get(7).getValue());
    }

    public TransactionReceipt modifyInvoice(BigInteger _totalAmount, String _description, BigInteger _creationDate, BigInteger _dueDate) {
        final Function function = new Function(
                FUNC_MODIFYINVOICE,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_totalAmount),
                        new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_description),
                        new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_creationDate),
                        new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_dueDate)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] modifyInvoice(BigInteger _totalAmount, String _description, BigInteger _creationDate, BigInteger _dueDate, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_MODIFYINVOICE,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_totalAmount),
                        new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_description),
                        new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_creationDate),
                        new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_dueDate)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForModifyInvoice(BigInteger _totalAmount, String _description, BigInteger _creationDate, BigInteger _dueDate) {
        final Function function = new Function(
                FUNC_MODIFYINVOICE,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_totalAmount),
                        new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_description),
                        new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_creationDate),
                        new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_dueDate)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple4<BigInteger, String, BigInteger, BigInteger> getModifyInvoiceInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_MODIFYINVOICE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple4<BigInteger, String, BigInteger, BigInteger>(

                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (BigInteger) results.get(2).getValue(),
                (BigInteger) results.get(3).getValue()
        );
    }

    public TransactionReceipt modifyStatus(BigInteger invoiceStatus) {
        final Function function = new Function(
                FUNC_MODIFYSTATUS,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint8(invoiceStatus)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] modifyStatus(BigInteger invoiceStatus, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_MODIFYSTATUS,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint8(invoiceStatus)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForModifyStatus(BigInteger invoiceStatus) {
        final Function function = new Function(
                FUNC_MODIFYSTATUS,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint8(invoiceStatus)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<BigInteger> getModifyStatusInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_MODIFYSTATUS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
        );
    }

    public String owner() throws ContractException {
        final Function function = new Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public static Invoice load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Invoice(contractAddress, client, credential);
    }

    public static Invoice deploy(Client client, CryptoKeyPair credential, String _creator, String _recipient, BigInteger _totalAmount, String _description, BigInteger _creationDate, BigInteger _dueDate, String _auditInfo) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(_creator),
                new org.fisco.bcos.sdk.abi.datatypes.Address(_recipient),
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_totalAmount),
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_description),
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_creationDate),
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_dueDate),
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_auditInfo)));
        return deploy(Invoice.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }

    public static class InvoiceInfo extends DynamicStruct {
        public String creator;

        public String recipient;

        public BigInteger totalAmount;

        public String description;

        public BigInteger creationDate;

        public BigInteger dueDate;

        public BigInteger status;

        public String auditInfo;

        public InvoiceInfo(Address creator, Address recipient, Uint256 totalAmount, Utf8String description, Uint256 creationDate, Uint256 dueDate, Uint8 status, Utf8String auditInfo) {
            super(creator,recipient,totalAmount,description,creationDate,dueDate,status,auditInfo);
            this.creator = creator.getValue();
            this.recipient = recipient.getValue();
            this.totalAmount = totalAmount.getValue();
            this.description = description.getValue();
            this.creationDate = creationDate.getValue();
            this.dueDate = dueDate.getValue();
            this.status = status.getValue();
            this.auditInfo = auditInfo.getValue();
        }

        public InvoiceInfo(String creator, String recipient, BigInteger totalAmount, String description, BigInteger creationDate, BigInteger dueDate, BigInteger status, String auditInfo) {
            super(new org.fisco.bcos.sdk.abi.datatypes.Address(creator),new org.fisco.bcos.sdk.abi.datatypes.Address(recipient),new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(totalAmount),new org.fisco.bcos.sdk.abi.datatypes.Utf8String(description),new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(creationDate),new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(dueDate),new org.fisco.bcos.sdk.abi.datatypes.generated.Uint8(status),new org.fisco.bcos.sdk.abi.datatypes.Utf8String(auditInfo));
            this.creator = creator;
            this.recipient = recipient;
            this.totalAmount = totalAmount;
            this.description = description;
            this.creationDate = creationDate;
            this.dueDate = dueDate;
            this.status = status;
            this.auditInfo = auditInfo;
        }
    }

    public static class InvoiceCreatedEventResponse {
        public TransactionReceipt.Logs log;

        public byte[] invoiceId;

        public String creator;

        public String recipient;

        public BigInteger totalAmount;

        public String description;

        public BigInteger creationDate;

        public BigInteger dueDate;
    }

    public static class InvoiceUpdateEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger totalAmount;

        public String description;

        public BigInteger creationDate;

        public BigInteger dueDate;
    }
}