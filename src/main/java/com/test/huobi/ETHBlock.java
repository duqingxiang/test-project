package com.test.huobi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ETHBlock {

    // 区块高度
    private String number;
    // 区块hash
    private String hash;
    // 指向父区块(parentBlock)的指针。除了创世块(Genesis Block)外，每个区块有且只有一个父区块
    private String parentHash;
    // 一个64bit的哈希数，它被应用在区块的"挖掘"阶段，并且在使用中会被修改
    private String nonce;
    // 叔伯节点的区块数据 （SHA3 of the uncles data in the block.）
    private String sha3Uncles;
    // Bloom过滤器(Filter)，用来快速判断一个参数Log对象是否存在于一组已知的Log集合中。（the bloom filter for the logs of the block. null when its pending block.）
    private String logsBloom;
    // StateDB中的“state Trie”的根节点的RLP哈希值。
    // Block中，每个账户以stateObject对象表示，账户以Address为唯一标示，其信息在相关交易(Transaction)的执行中被修改。
    // 所有账户对象可以逐个插入一个Merkle-PatricaTrie(MPT)结构里，形成“state Trie”。
    // Root，TxHash和ReceiptHash，分别取自三个MPT类型对象：stateTrie, txTrie, 和receiptTrie的根节点哈希值。
    // 用一个32byte的哈希值，来代表一个有若干节点的树形结构（或若干元素的数组），这是为了加密。
    // 比如在Block的同步过程中，通过比对收到的TxHash，可以确认数组成员transactions是否同步完整。

    // the root of the transaction trie of the block.
    private String transactionsRoot;
    // the root of the final state trie of the block.
    private String stateRoot;
    // the root of the receipts trie of the block.
    private String receiptsRoot;
    // 获得挖矿收益的人的地址（the address of the beneficiary to whom the mining rewards were given.）
    private String miner;
    private String mixHash;
    // 挖矿难度（ integer of the difficulty for this block.）
    private String difficulty;
    // 整个链到当前区块的总难度（ integer of the total difficulty of the chain until this block.）
    private String totalDifficulty;
    // 区块上的额外信息（the "extra data" field of this block.）
    private String extraData;
    // 区块大小（integer the size of this block in bytes.）
    private String size;
    // 区块内所有Gas消耗的理论上限 (the maximum gas allowed in this block.)
    private String gasLimit;
    // 区块内所有Transaction执行时所实际消耗的Gas总和。(the total used gas by all transactions in this block.)
    private String gasUsed;
    // 区块被收录的unix时间戳 (the unix timestamp for when the block was collated.)
    private String timestamp;
    // 区块上的所有交易
    private List<ETHTransaction> transactions;
    // 叔伯节点的hash（Array of uncle hashes）
    private List<String> uncles;

}
