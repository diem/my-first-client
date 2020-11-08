import {bytesToHexString} from "@libra/client/dist/utils/bytes";
import {Signer} from "@libra/client/dist/utils/signer";
import AccountKeys from "@libra/client/dist/account/accountKeys";
import {TestnetClient} from "@libra/client";

/**
 * getAccountExample demonstrates the required operation to retrieve account information from the Libra blockchain
 */

async function main() {
  //generate keys
  const keyPair = Signer.generateKeyPair();
  const accountKeys = new AccountKeys(keyPair);

  //connect to client
  const client = new TestnetClient();

  //create account
  await client.mint(bytesToHexString(accountKeys.authKey), BigInt(1340000000), "Coin1")

  //get account information
  const account = await client.getAccount(accountKeys.accountAddress)

  console.log("~ Account info: %O", account);
}

main();
