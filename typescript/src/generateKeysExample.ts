// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import {AccountKeys, KeyPair, Signer} from "@libra/client";

/**
 * generateKeysExample demonstrates the required steps to generate keys for an account on the Libra blockchain
 */
function main() {
  const keyPair = generateKeyPair();
  const accountKeys = generateAccountKeys(keyPair);


  console.log("~ Auth key: ", accountKeys.authKey);
  console.log('~ Private Key: ', keyPair.privateKey.toString("hex"));
  console.log('~ Public Key: ', keyPair.publicKey.toString("hex"));
}

export function generateKeyPair(): KeyPair {
  return Signer.generateKeyPair();
}

export function generateAccountKeys(keyPair?) {
  if (!keyPair) {
    keyPair = generateKeyPair();
  }

  return new AccountKeys(keyPair);
}

main();
