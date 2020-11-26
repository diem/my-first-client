import {bytesToHexString} from "@libra/client/dist/utils/bytes";
import {Signer} from "@libra/client/dist/utils/signer";
import AccountKeys from "@libra/client/dist/account/accountKeys";

/**
 * generateKeysExample demonstrates the required steps to generate keys for an account on the Diem blockchain
 */
function main() {
  const keyPair = Signer.generateKeyPair();
  const accountKeys = new AccountKeys(keyPair);

  console.log("~ Private Key: %s", bytesToHexString(accountKeys.privateKey));
  console.log("~ Public Key: %s", bytesToHexString(accountKeys.publicKey));
  console.log("~ Auth Key: %s", bytesToHexString(accountKeys.authKey));
  console.log("~ Address: %s", bytesToHexString(accountKeys.accountAddress));
}

main();
