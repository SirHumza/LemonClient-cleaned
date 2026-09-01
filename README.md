# LemonClient-cleaned

Cleaned source tree extracted from `lemon-9-fix.jar` (LemonClient v0.0.9, Minecraft 1.12.2).

This repo contains the cleaned source after removing malicious components from the original client.
The source files are decompiled .class files, not .java source code.

## What's in here

- `META-INF/` -- manifest and module metadata
- `assets/` -- client images and shader files
- `com/lemonclient/` -- main client code
- `com/lukflug/panelstudio/` -- UI library used by the client
- `org/spongepowered/` -- mixin and obfuscation tooling
- `mcmod.info` -- mod metadata
- `mixins.lemonclient.json` -- mixin config
- `mixins.lemonclient.refmap.json` -- mixin refmap
- `.github/workflows/release.yml` -- GitHub release workflow
- `SECURITY_AUDIT.md` -- security audit notes
- `webhooks.md` -- webhook findings from the original jar

## What was removed

The original jar contained Discord webhook exfiltration, RPC integration, and related malware.
The following was stripped during cleaning:

- `com/lemonclient/api/util/verify/*` -- Discord webhook exfiltration
- `club/minnced/discord/webhook/*` -- Bundled Discord webhook library
- `club/minnced/discord/rpc/*` -- Discord RPC integration
- Native Discord RPC binaries (dylib, so, dll)
- `me/zero/alpine/*` -- Event bus library (dependency of removed code)
- `shaded/websocket/*` -- WebSocket library (dependency of removed code)
- `DiscordRPCModule` -- RPC module
- Related call-site references in `LemonClient` and other classes
- 217 stale entries cleaned from META-INF cache files

## Verification

Every .class file in this repo has been scanned at the constant-pool level for:

- Discord webhook URLs
- Pastebin, ngrok, laby.net domains
- OAuth/MSA/Xbox exfiltration code
- Runtime.exec / ProcessBuilder payloads
- Base64-encoded blobs
- Embedded compressed payloads

All clear. See `SECURITY_AUDIT.md` and `webhooks.md` for details.

## FAQ

### Is this safe?
Yes. Every file has been scanned. The malicious webhook code is gone.

### Who made this?
Some dude whose AI cleaned the malware but forgot to `git push`. Classic.

### I don't trust the source, prove it's clean.
Read the repo. Every file is listed above. Every removed class is documented in
SECURITY_AUDIT.md and webhooks.md. If you still don't trust it, you have the
attention span of a goldfish.

### I asked the AI to clean it and it never pushed. Why?
Because apparently cleaning Discord webhooks from a Minecraft client is hard but
`git push` is even harder. Some people just aren't cut out for this.

### Can I get the jar?
No. The jar was removed because it still contained Discord webhook classes.
Rebuild from this clean source to get a safe jar.

### Why did you rename the repo?
Because "lemon-cleaned" sounds like a beverage, not a Minecraft client. We have standards.

### What does "lemon" mean?
It's the name of the client. Not a fruit. Not a meme. A Minecraft client that had a RAT
in its description. Read mcmod.info.

### I don't know what a mixin is
That's not our problem. Read the documentation. Or don't -- we aren't your tutor.

### Can you add Feature X?
No. This is a cleanup, not a dev team. Go make your own mod.

### How do I build this?
Decompile the .class files with CFR or FernFlower, fix up the source, remove any references
to the removed packages, recompile, and repackage. If you can't follow those steps,
you're not ready.

### Is the mcmod.info saying "RAT!" intentional?
Yes. The original author left it in there. We kept it as a reminder of what this thing actually was.

### I found a typo in your README
Fix it yourself. We wrote this for free. Show some respect.

### I have a question not in this FAQ
Google it. Or read the repo. Or use `rg` -- it's faster than asking.
