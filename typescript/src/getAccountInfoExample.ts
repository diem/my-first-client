// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import {LibraClient} from "@libra/client";
import {TEST_NET, testnetJsonRPCUrl} from "./testnet";
import {minter} from "./mintExample";
import {generateAccountKeys} from "./generateKeysExample";

/**
 * get_account_info_example demonstrates the required operation to retrieve account information from the Libra blockchain
 */

async function main() {
  //create account
  const accountKeys = generateAccountKeys();
  const address = await minter(accountKeys);

  //get account information
  const accountInfo = await getAccountInfo(address);

  console.log("~ Account info:");
  console.log(accountInfo);

}

export async function getAccountInfo(address: string) {
  try {
    return await new LibraClient(testnetJsonRPCUrl, TEST_NET).getAccount(address);
  } catch (e) {
    throw new Error('Failed to get account from client: ' + e);
  }
}

main();
