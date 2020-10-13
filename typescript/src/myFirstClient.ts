/**
 * This code demonstrates basic flow for working with the LibraClient.
 * 1. Generate keys
 * 2. Create account - by minting
 * 3. Get account information
 * 4. Start events listener
 * 5. Add money to existing account (mint)
 * 6. Generate keys for second account
 * 7. Create second account (for the benefit of the following transaction)
 * 8. Generate IntentIdentifier
 * 9. Deserialize IntentIdentifier
 * 10. Transfer money between accounts (peer to peer transaction)
 */
import {generateAccountKeys, generateKeyPair} from "./generateKeysExample";
import {minter} from "./mintExample";
import {getAccountInfo} from "./getAccountInfoExample";

export async function main(): Promise<void> {
  console.log("#1 Generate Keys")
  const senderKeyPair = generateKeyPair()
  const senderAccountKeys = generateAccountKeys(senderKeyPair)

  console.log("#2 Create account")
  const senderAccountAddress = await minter(senderAccountKeys, 1340000000, "LBR")

  console.log("#3 Get account information");
  const senderAccount = await getAccountInfo(senderAccountAddress);

  const eventsKey = senderAccount.received_events_key;

  console.log("#4 Start event listener")
  // subscribe(events_key)

  console.log("#5 Add money to account")
  // mint(sender_auth_key.hex(), 270000000, "LBR")

  console.log("#6 Generate Keys")
  // receiver_private_key = generate_private_key()
  // receiver_auth_key: AuthKey = generate_auth_key(receiver_private_key)

  console.log("#7 Create second account")
  // mint(receiver_auth_key.hex(), 2560000000, "LBR")

  console.log("#8 Generate IntentIdentifier")
  // encoded_intent_identifier = generate_intent_identifier(receiver_auth_key.account_address(), 130000000, "LBR")

  console.log("#9 Deserialize IntentIdentifier")
  // intent_identifier = decode_intent(encoded_intent_identifier)

  console.log("#10 Peer 2 peer transaction")
  /*
    submit_peer_to_peer_transaction(sender_private_key,
                                    intent_identifier.amount,
                                    receiver_auth_key.account_address(),
                                    sender_auth_key.account_address(),
                                    sender_account.sequence_number,
                                    "LBR")
   */
}
