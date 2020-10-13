import {TEST_NET, testnetJsonRPCUrl} from "./testnet";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import {LibraClient} from "@libra/client";

export function subscribe(eventsKey) {
  let start=0;
  await new LibraClient(testnetJsonRPCUrl, TEST_NET).getEvents(eventsKey, start, 10);
}
