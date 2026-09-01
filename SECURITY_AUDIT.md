# Security Audit — lemon-cleaned

## Scope

- `lemon-9-fix.jar`
- Cleaned source tree in this repo

## What was removed

- `com/lemonclient/api/util/verify/*`
  - Discord webhook exfiltration
  - Startup/shutdown telemetry
  - HWID fingerprinting
  - Remote list fetching
  - Custom HTTPS client with trust-all SSL

- `club/minnced/discord/webhook/*`
  - Bundled Discord webhook library

- `club/minnced/discord/rpc/*`
  - Discord RPC integration and native library loader

- Native Discord RPC binaries
  - `darwin/libdiscord-rpc.dylib`
  - `linux-x86-64/libdiscord-rpc.so`
  - `win32-x86-64/discord-rpc.dll`
  - `win32-x86/discord-rpc.dll`

- `me/zero/alpine/*`
- `shaded/websocket/*`
- `DiscordRPCModule`
- Related call-site references in `LemonClient` and other classes

## Verification results

- No `club/minnced` paths remain
- No `me/zero/alpine` paths remain
- No `shaded/websocket` paths remain
- No `com/lemonclient/api/util/verify` paths remain
- No Discord RPC native binaries remain
- No `discord.com/api/webhooks` strings remain
- No `discord.com/api/v8/webhooks` strings remain
- No `cdn.discordapp.com/attachments` strings remain

## How to audit independently

1. Clone this repo.
2. Search the source tree for any remaining webhook, hostname, or download
   strings.
3. Decompile the release jar and repeat the same search.
4. Run a network-call scanner on the rebuilt jar.

## Status

Unsafe paths have been removed. This is a cleaned, auditable artifact, not a
feature-complete open-source client.
