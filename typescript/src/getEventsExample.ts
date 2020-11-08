import {TestnetClient} from "@libra/client";
import {bytesToHexString} from "@libra/client/dist/utils/bytes";
import AccountKeys from "@libra/client/dist/account/accountKeys";
import {Signer} from "@libra/client/dist/utils/signer";
import {delay, subscribe} from "./eventsSubscriber";

const CURRENCY = "Coin1";

/**
 * getEventsExample demonstrates how to subscribe to a specific events stream base on events key
 */
async function main() {
  // generate keys
  const keyPair = Signer.generateKeyPair();
  const accountKeys = new AccountKeys(keyPair);

  //connect to client
  const client = new TestnetClient();

  // create new account
  await client.mint(bytesToHexString(accountKeys.authKey), BigInt(1340000000), CURRENCY)
  console.log("~ Account address: %s", bytesToHexString(accountKeys.accountAddress))

  // get account events key
  let account = await client.getAccount(accountKeys.accountAddress)

  let retries = 0;
  const maxRetries = 25;

  while (!account && retries < maxRetries) {
    console.log("~ no account was found for address %s retry #%i", bytesToHexString(accountKeys.accountAddress), retries + 1)
    retries++;

    await delay(2);

    account = await client.getAccount(accountKeys.accountAddress)
  }

  const eventsKey = account.received_events_key;

  // start minter to demonstrates events creation
  mint(client, accountKeys)

  // demonstrates events subscription
  await subscribe(client, eventsKey);
}


async function mint(client: TestnetClient, accountKeys: AccountKeys) {
  for (let i = 0; i < 15; i++) {
    const amount = (Math.floor(Math.random() * 19) + 10) * 10000000
    console.log("~ Minting %i coins", amount)
    await client.mint(bytesToHexString(accountKeys.authKey), BigInt(amount), "Coin1")
    await delay(1)
  }
}


main();
