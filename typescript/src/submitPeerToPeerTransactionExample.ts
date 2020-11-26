import {Signer} from "@libra/client/dist/utils/signer";
import AccountKeys from "@libra/client/dist/account/accountKeys";
import {LibraUtils, TestnetClient} from "@libra/client";
import {bytesToHexString} from "@libra/client/dist/utils/bytes";
import {Stdlib} from "@libra/client/dist/lcs/libraStdlib";
import {ChainId, RawTransaction, TransactionPayloadVariantScript} from "@libra/client/dist/lcs/libraTypes";
import {LcsSerializer} from "@libra/client/dist/lcs/lcs/lcsSerializer";
import {delay} from "./eventsSubscriber";

const CURRENCY = "Coin1";

/**
 * submitPeerToPeerTransactionExample demonstrates currencies transfer between 2 accounts on the Diem blockchain
 */
async function main() {
  //generate sender keys
  const senderKeyPair = Signer.generateKeyPair();
  const senderAccountKeys = new AccountKeys(senderKeyPair);

  //connect to client
  const client = new TestnetClient();

  //create sender account
  const senderAuthKeyHex = bytesToHexString(senderAccountKeys.authKey);
  await client.mint(senderAuthKeyHex, BigInt(1340000000), CURRENCY)
  const senderAccountAddress = senderAccountKeys.accountAddress;
  const senderAccountAddressHex = bytesToHexString(senderAccountAddress);
  console.log("~ Sender address: %s", senderAccountAddressHex)

  //get sender account
  let senderAccount = await client.getAccount(senderAccountAddress)

  let retries = 0;
  const maxRetries = 25;

  while (!senderAccount && retries < maxRetries) {
    console.log("~ no account was found for address %s retry #%i", senderAccountAddressHex, retries + 1)
    retries++;

    await delay(2);

    senderAccount = await client.getAccount(senderAccountAddress)
  }

  //generate receiver keys
  const receiverKeyPair = Signer.generateKeyPair();
  const receiverAccountKeys = new AccountKeys(receiverKeyPair);

  //create receiver account
  const receiverAuthKey = receiverAccountKeys.authKey;
  const receiverAuthKetHex = bytesToHexString(receiverAuthKey);
  await client.mint(receiverAuthKetHex, BigInt(10000000), CURRENCY)
  const receiverAccountAddressHex = bytesToHexString(receiverAccountKeys.accountAddress);
  console.log("~ Receiver address: %s", receiverAccountAddressHex)

  //create script
  console.log("~ create script")
  const [metadata, metadataSignature] = LibraUtils.createGeneralMetadata(null, null, null);

  const currencyTag = LibraUtils.makeCurrencyTypeTag(CURRENCY);

  const script = Stdlib.encodePeerToPeerWithMetadataScript(
    currencyTag,
    LibraUtils.makeAccountAddress(receiverAccountAddressHex),
    BigInt(130000000),
    metadata,
    metadataSignature
  );

  //create transaction
  const expirationTime = BigInt(Math.trunc(new Date().getTime() / 1000 + 60 * 100));

  const senderSequenceNumber = senderAccount.sequence_number;
  console.log("~ senderSequenceNumber: %i", senderSequenceNumber);

  const transaction = new RawTransaction(
    LibraUtils.makeAccountAddress(senderAccountAddressHex),
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
  console.log("~ submit transaction")
  try {
    await client.submitRawSignedTransaction(hexTransaction);
  } catch (e) {
    console.error(e)
  }
  //wait for transaction
  console.log("~ wait for transaction")
  const transactionHash = LibraUtils.hashTransaction(signedBytes);

  try {
    await client.waitForTransaction(senderAccountAddress, senderSequenceNumber, false, transactionHash, expirationTime)
  } catch (e) {
    console.error(e)
  }
}

main()
