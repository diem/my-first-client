// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import {AccountKeys, Currency, mint} from "@libra/client";
import {generateAccountKeys} from "./generateKeysExample";

/**
 * mintExample demonstrates how to add currencies to account on the Libra blockchain
 * The mint also use to create new account by adding currencies base on new auth_key
 */
async function main() {
  const accountKeys = generateAccountKeys();
  //use mint to create new account
  const address = await minter(accountKeys, 192000000, "LBR");

  console.log("Account address: " + address);

}

export const minter = async (
  accountKeys: AccountKeys,
  amount = 1000000,
  currency: Currency = 'LBR'
): Promise<string> => {
  try {
    const transaction = await mint(accountKeys, amount, currency);

    return transaction.transaction.script.receiver;
  } catch (e) {
    throw new Error('Failed to send create account request to client' + e);
  }
};

main();
