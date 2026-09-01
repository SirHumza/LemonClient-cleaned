# Webhook findings — lemon-cleaned

## Source inspected

- `lemon-9-fix.jar`
- Cleaned source tree in this repo

## Discord webhooks found in the original jar

- `https://discord.com/api/webhooks/test`
  - `com/lemonclient/api/util/verify/End.class`
  - `com/lemonclient/api/util/verify/Nigger.class`

- `https://discord.com/api/v8/webhooks/%s/%s`
  - `club/minnced/discord/webhook/WebhookClient.class`

- Library attribution
  - `https://github.com/MinnDevelopment/discord-webhooks`
  - `club/minnced/discord/webhook/WebhookClient.class`

- Attachment URLs used by verify classes
  - `https://cdn.discordapp.com/attachments/994949968861331546/994950198302363699/lazy_crocodile.png`
    - `com/lemonclient/api/util/verify/Manager.class`
  - `https://cdn.discordapp.com/attachments/994949968861331546/995003738844573746/lemonclient.png`
    - `com/lemonclient/api/util/verify/End.class`
    - `com/lemonclient/api/util/verify/Nigger.class`

## Status in this repo

All of the above webhook code, library code, attachment URLs, and call-site
references have been removed from this repo.

## Independent check

Search the repo for:
- `discord.com/api/webhooks`
- `discord.com/api/v8/webhooks`
- `cdn.discordapp.com/attachments`
- `club/minnced/discord/webhook`
- `com/lemonclient/api/util/verify`

None of those should remain.
