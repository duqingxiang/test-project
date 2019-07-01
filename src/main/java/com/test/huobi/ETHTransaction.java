package com.test.huobi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ETHTransaction {

    // 交易hash (hash of the transaction.)
    private String hash;
    // 交易发送者给这个交易所带的数字（也可能是一些信息啥的） (the number of transactions made by the sender prior to this one)
    private String nonce;
    // 区块的hash (hash of the block where this transaction was in. null when its pending.)
    private String blockHash;
    // 区块高度 （block number where this transaction was in. null when its pending.）
    private String blockNumber;
    // 交易在区块中的位置 （ integer of the transactions index position in the block. null when its pending.）
    private String transactionIndex;
    // 转账发起者 （address of the sender.）
    private String from;
    // 转账收入者  （address of the receiver. null when its a contract creation transaction）
    private String to;
    // 转账数量 单位：Wei （value transferred in Wei）
    private String value;
    // 发起转账者支付的gas的价格 (gas price provided by the sender in Wei.)
    private String gasPrice;
    // 支付的gas数量 ( gas provided by the sender.)
    private String gas;
    // 交易的数据 (the data send along with the transaction)
    private String input;

}
