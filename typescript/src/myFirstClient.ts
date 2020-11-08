import {LibraUtils, TestnetClient} from "@libra/client";
import {Signer} from "@libra/client/dist/utils/signer";
import AccountKeys from "@libra/client/dist/account/accountKeys";
import {bytesToHexString} from "@libra/client/dist/utils/bytes";
import Intent from "@libra/client/dist/utils/intent";
import {Stdlib} from "@libra/client/dist/lcs/libraStdlib";
import {ChainId, RawTransaction, TransactionPayloadVariantScript} from "@libra/client/dist/lcs/libraTypes";
import {LcsSerializer} from "@libra/client/dist/lcs/lcs/lcsSerializer";
import {delay, subscribe} from "./eventsSubscriber";

const CURRENCY = "Coin1";

/**
 * This code demonstrates basic flow for working with the LibraClient.
 * 1. connect to testnet
 * 2. Generate keys
 * 3. Create account - by minting
 * 4. Get account information
 * 5. Start events listener
 * 6. Add money to existing account (mint)
 * 7. Generate keys for second account
 * 8. Create second account (for the benefit of the following transaction)
 * 9. Generate IntentIdentifier
 * 10. Deserialize IntentIdentifier
 * 11. Transfer money between accounts (peer to peer transaction)
 */

export async function main(): Promise<void> {
  console.log("#1 connect to testnet")
  const client = new TestnetClient();

  console.log("#2 Generate keys for sender account")
  const senderKeyPair = Signer.generateKeyPair();
  const senderAccountKeys = new AccountKeys(senderKeyPair);
  console.log("~ Sender address: %s", bytesToHexString(senderAccountKeys.accountAddress));

  console.log("#3 Create account")
  await client.mint(bytesToHexString(senderAccountKeys.authKey), BigInt(1340000000), CURRENCY);

  console.log("#4 Get sender account")
  let senderAccount = await client.getAccount(senderAccountKeys.accountAddress)

  let retries = 0;

  while (!senderAccount) {
    console.log("no account was found for address %s retry #%i", bytesToHexString(senderAccountKeys.accountAddress), retries + 1)
    retries++;

    await delay(2);

    senderAccount = await client.getAccount(senderAccountKeys.accountAddress)
  }

  const eventsKey = senderAccount.received_events_key;

  console.log("#5 Start event listener")
  subscribe(client, eventsKey);

  console.log("#6 Add money to sender account")
  await client.mint(bytesToHexString(senderAccountKeys.authKey), BigInt(270000000), CURRENCY);

  console.log("#7 Generate keys for receiver account")
  const receiverKeyPair = Signer.generateKeyPair();
  const receiverAccountKeys = new AccountKeys(receiverKeyPair);
  console.log("~ Receiver address: %s", bytesToHexString(receiverAccountKeys.accountAddress));

  console.log("#8 Create receiver account")
  await client.mint(bytesToHexString(receiverAccountKeys.authKey), BigInt(2560000000), CURRENCY);

  console.log("#9 Generate IntentIdentifier")
  const encodeIntent = LibraUtils.encodeIntent(new Intent('tlb', bytesToHexString(receiverAccountKeys.accountAddress), undefined, "Coin1", 130000000));
  console.log("~ Encoded IntentIdentifier: %s", encodeIntent.href)

  console.log("#10 Deserialize IntentIdentifier")
  const decodeIntent = LibraUtils.decodeIntent("tlb", encodeIntent);
  console.log("~ Account (HEX) from intent: %s", decodeIntent.address);
  console.log("~ Amount from intent: %s", decodeIntent.amount);
  console.log("~ Currency from intent: %s", decodeIntent.currency);

  console.log("#11 Peer 2 peer transaction")
  //create script
  const [metadata, metadataSignature] = LibraUtils.createGeneralMetadata(null, null, null);

  const script = Stdlib.encodePeerToPeerWithMetadataScript(
    LibraUtils.makeCurrencyTypeTag(decodeIntent.currency),
    LibraUtils.makeAccountAddress(decodeIntent.address),
    BigInt(decodeIntent.amount),
    metadata,
    metadataSignature
  );

  //create transaction
  const expirationTime = BigInt(Math.trunc(new Date().getTime() / 1000 + 60 * 100));

  const senderSequenceNumber = senderAccount.sequence_number;

  const transaction = new RawTransaction(
    LibraUtils.makeAccountAddress(bytesToHexString(senderAccountKeys.accountAddress)),
    senderSequenceNumber,
    new TransactionPayloadVariantScript(script),
    BigInt(1000000),
    BigInt(0),
    CURRENCY,
    expirationTime,
    new ChainId(2)
  );

  //sign transaction
  const signedTransaction = LibraUtils.signTransaction(transaction, senderAccountKeys);

  const signedTxSerializer = new LcsSerializer();
  signedTransaction.serialize(signedTxSerializer);

  const signedBytes = signedTxSerializer.getBytes();
  const hexTransaction = bytesToHexString(signedBytes);

  //submit transaction
  try {
    await client.submitRawSignedTransaction(hexTransaction);
  } catch (e) {
    console.error(e)
  }

  //wait for transaction
  const transactionHash = LibraUtils.hashTransaction(signedBytes);

  try {
    await client.waitForTransaction(senderAccountKeys.accountAddress, senderSequenceNumber, false, transactionHash, expirationTime)
  } catch (e) {
    console.error(e)
  }
}

main();
