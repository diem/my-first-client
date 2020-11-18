/**
 * intentIdentifierExample demonstrates the IntentIdentifier generation and deserialization
 */
import {LibraUtils} from "@libra/client";
import Intent from "@libra/client/dist/utils/intent";
import {Signer} from "@libra/client/dist/utils/signer";
import AccountKeys from "@libra/client/dist/account/accountKeys";
import {bytesToHexString} from "@libra/client/dist/utils/bytes";

const HRP = 'tlb';
const CURRENCY = "Coin1";

async function main() {
  //generate keys
  const keyPair = Signer.generateKeyPair();
  const accountKeys = new AccountKeys(keyPair);

  //create intent
  const intent = new Intent(HRP, bytesToHexString(accountKeys.accountAddress), undefined, CURRENCY, 130000000);
  //encode
  const encodeIntent = LibraUtils.encodeIntent(intent);
  console.log("~ intent identifier: %s", encodeIntent.href);

  //deserialize IntentIdentifier
  const decodeIntent = LibraUtils.decodeIntent(HRP, encodeIntent);
  console.log("~ Account (HEX) from intent: %s", decodeIntent.address);
  console.log("~ Amount from intent: %s", decodeIntent.amount);
  console.log("~ Currency from intent: %s", decodeIntent.currency);
}

main();
