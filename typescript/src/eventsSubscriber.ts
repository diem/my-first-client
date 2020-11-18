import {TestnetClient} from "@libra/client";

export async function subscribe(client: TestnetClient, eventsKey: string) {
  let start = 0;

  for (let i = 0; i < 15; i++) {
    const events = await client.getEvents(eventsKey, BigInt(start), 10);
    start += events.length;
    console.log("~ %i new events found", events.length)
    await delay(2)

    for (let j = 0; j < events.length; j++) {
      console.log("~ Event # : %i", j + 1)
      console.log(events[j])
    }
  }
}


export async function delay(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms * 100));
}
