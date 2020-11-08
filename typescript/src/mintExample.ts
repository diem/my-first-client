/**
 * mintExample demonstrates how to add currencies to account on the Libra blockchain
 * The mint also use to create new account by adding currencies base on new auth_key
 */
import {Signer} from "@libra/client/dist/utils/signer";
import AccountKeys from "@libra/client/dist/account/accountKeys";
import {TestnetClient} from "@libra/client";
import {bytesToHexString} from "@libra/client/dist/utils/bytes";

const CURRENCY = "Coin1";

/**
 * mintExample demonstrates how to add currencies to account on the Libra blockchain testnet
 * The mint also use to create new account by adding currencies base on new auth_key
 */
async function main() {
  //connect to client
  const client = new TestnetClient();

  //generate keys
  const keyPair = Signer.generateKeyPair();
  const accountKeys = new AccountKeys(keyPair);

  //use mint to create new account
  await client.mint(bytesToHexString(accountKeys.authKey), BigInt(192000000), CURRENCY)

  console.log("Account address: %s", bytesToHexString(accountKeys.accountAddress));
}


main();
