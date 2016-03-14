package cn.kisor.rpc.constant;
public interface Constant {

    int ZK_SESSION_TIMEOUT = 5000;
	int RPC_TIMEOUT = 5000;
    String ZK_REGISTRY_PATH = "/registry";
	String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
}