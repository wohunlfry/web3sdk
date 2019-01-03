package org.fisco.bcos.channel.test.precompile;

import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

public class UpdatePBFTNode {
    private static BigInteger gasPrice = new BigInteger("300000000");
    private static BigInteger gasLimit = new BigInteger("300000000");
    private static BigInteger initialWeiValue = new BigInteger("0");
    private static String MinerPrecompileAddress = "0x0000000000000000000000000000000000001003";

    public void call(String[] args, Web3j web3j, Credentials credentials, int groupId) throws Exception {
        /// get functions
        if (args.length < 1)
            Usage(args);
        String operation = args[1];
        if (args.length < 3)
            Usage(args);
        String nodeId = args[2];
        if (operation.equals("addMiner")) {
            System.out.println("==== addMiner " + nodeId + " of " + groupId);
            AddNodeToMiner(nodeId, web3j, credentials);
            System.out.println("==== addMiner " + nodeId + " of " + groupId + " END ====");
            System.exit(0);
        }
        if (operation.equals("addObserver")) {
            System.out.println("==== addObserver " + nodeId + " of " + groupId);
            AddNodeToObserver(nodeId, web3j, credentials);
            System.out.println("==== addObserver " + nodeId + " of " + groupId + " END ====");
            System.exit(0);
        }
        if (operation.equals("remove")) {
            System.out.println("==== remove " + nodeId + " of " + groupId);
            RemoveNode(nodeId, web3j, credentials);
            System.out.println("==== remove " + nodeId + " of " + groupId + " END ====");
            System.exit(0);
        }
    }

    private void Usage(String[] args) {
        System.out.println("Usage:");
        System.out.println("java -cp 'conf/:apps/*:lib/*' org.fisco.bcos.channel.test.PrecompileManager pbft addMiner ${nodeId}");
        System.out.println("java -cp 'conf/:apps/*:lib/*' org.fisco.bcos.channel.test.PrecompileManager pbft addObserver ${nodeId}");
		System.out.println("java -cp 'conf/:apps/*:lib/*' org.fisco.bcos.channel.test.PrecompileManager pbft remove ${nodeId}");
        System.exit(0);
    }

    public int AddNodeToMiner(String nodeId, Web3j web3j, Credentials credentials) throws Exception {
        return AddMiner(MinerPrecompileAddress, web3j, credentials, nodeId);
    }

    public int AddNodeToObserver(String nodeId, Web3j web3j, Credentials credentials) throws Exception {
        return AddObserver(MinerPrecompileAddress, web3j, credentials, nodeId);
    }
	
	public int RemoveNode(String nodeId, Web3j web3j, Credentials credentials) throws Exception {
       return Remove(MinerPrecompileAddress, web3j, credentials, nodeId);
    }

    private int AddMiner(String address, Web3j web3j, Credentials credentials, String nodeId) throws Exception {
        ConsensusSystemTable consensus = ConsensusSystemTable.load(address, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt receipt = consensus.addMiner(nodeId).send();
		int result = 0;
		if ("0xffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff".equals(receipt.getOutput())) {
			result = -1;
		} else {
			result = Integer.valueOf(receipt.getOutput().substring(2), 16).intValue();
		}
		return result;
    }

	private int AddObserver(String address, Web3j web3j, Credentials credentials, String nodeId) throws Exception {
        ConsensusSystemTable consensus = ConsensusSystemTable.load(address, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt receipt = consensus.addObserver(nodeId).send();
		int result = 0;
		if ("0xffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff".equals(receipt.getOutput())) {
			result = -1;
		} else {
			result = Integer.valueOf(receipt.getOutput().substring(2), 16).intValue();
		}
		return result;
    }
	
	private int Remove(String address, Web3j web3j, Credentials credentials, String nodeId) throws Exception {
        ConsensusSystemTable consensus = ConsensusSystemTable.load(address, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt receipt = consensus.remove(nodeId).send();
		int result = 0;
		if ("0xffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff".equals(receipt.getOutput())) {
			result = -1;
		} else {
			result = Integer.valueOf(receipt.getOutput().substring(2), 16).intValue();
		}
		return result;
    }
}