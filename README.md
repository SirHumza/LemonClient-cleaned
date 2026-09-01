# LemonClient-cleaned

Cleaned source tree extracted from `lemon-9-fix.jar` (LemonClient v0.0.9, Minecraft 1.12.2).

This repo contains the source that remains after removing the infected parts of
the original client. It is not a feature-complete open-source client.

## Repo layout

- `META-INF/` — manifest and module metadata
- `assets/` — client images and shader files
- `com/lemonclient/` — main client code
- `com/lukflug/panelstudio/` — UI library used by the client
- `org/spongepowered/` — mixin and obfuscation tooling
- `mcmod.info` — mod metadata
- `mixins.lemonclient.json` — mixin config
- `mixins.lemonclient.refmap.json` — mixin refmap
- `.github/workflows/release.yml` — GitHub release workflow
- `SECURITY_AUDIT.md` — security audit notes
- `webhooks.md` — webhook findings from the original jar

## What was removed

- `com/lemonclient/api/util/verify/*`
- `club/minnced/discord/webhook/*`
- `club/minnced/discord/rpc/*`
- Native Discord RPC binaries
- `me/zero/alpine/*`
- `shaded/websocket/*`
- `DiscordRPCModule`
- Related call-site references in `LemonClient` and other classes

## File listing

### META-INF
- `MANIFEST.MF`
- `fml_cache_annotation.json`
- `fml_cache_class_versions.json`
- `lemonclient_at.cfg`
- `maven/com.googlecode.json-simple/json-simple/pom.properties`
- `maven/com.googlecode.json-simple/json-simple/pom.xml`
- `maven/javax.websocket/javax.websocket-api/pom.properties`
- `maven/javax.websocket/javax.websocket-api/pom.xml`
- `maven/net.jodah/typetools/pom.properties`
- `maven/net.jodah/typetools/pom.xml`
- `services/javax.annotation.processing.Processor`
- `services/org.spongepowered.asm.service.IGlobalPropertyService`
- `services/org.spongepowered.asm.service.IMixinService`
- `services/org.spongepowered.asm.service.IMixinServiceBootstrap`
- `services/org.spongepowered.tools.obfuscation.service.IObfuscationService`

### assets
- `lemonclient/cape.png`
- `lemonclient/icons/icon-16x.png`
- `lemonclient/icons/icon-32x.png`
- `lemonclient/lemonclient.png`
- `lemonclient/shaders/fragment/aqua.frag`
- `lemonclient/shaders/fragment/aquaOutline.frag`
- `lemonclient/shaders/fragment/astralOutline.frag`
- `lemonclient/shaders/fragment/circle.frag`
- `lemonclient/shaders/fragment/circleOutline.frag`
- `lemonclient/shaders/fragment/default.frag`
- `lemonclient/shaders/fragment/fill.frag`
- `lemonclient/shaders/fragment/flow.frag`
- `lemonclient/shaders/fragment/glow.frag`
- `lemonclient/shaders/fragment/gradient.frag`
- `lemonclient/shaders/fragment/outlineGradient.frag`
- `lemonclient/shaders/fragment/phobos.frag`
- `lemonclient/shaders/fragment/rainbowCube.frag`
- `lemonclient/shaders/fragment/rainbowCubeOutline.frag`
- `lemonclient/shaders/fragment/smoke.frag`
- `lemonclient/shaders/fragment/smokeOutline.frag`
- `lemonclient/shaders/vertex.vert`

### com/lemonclient
- `api/config/LoadConfig.class`
- `api/config/SaveConfig.class`
- `api/event/LemonClientEvent$Era.class`
- `api/event/LemonClientEvent.class`
- `api/event/MultiPhase.class`
- `api/event/Phase.class`
- `api/event/events/*` — event classes
- `api/setting/Setting.class`
- `api/setting/SettingsManager.class`
- `api/setting/values/*` — setting value types
- `api/util/chat/*` — chat and notification utilities
- `api/util/font/*` — font utilities
- `api/util/log4j/*` — log4j fix utilities
- `api/util/misc/*` — misc utilities
- `api/util/player/*` — player utilities
- `api/util/player/social/*` — friend/ignore/social
- `api/util/render/*` — render utilities and shaders
- `api/util/world/*` — world and combat utilities
- `client/LemonClient.class`
- `client/PeekCmd*.class`
- `client/clickgui/*` — clickgui implementation
- `client/command/*` — command implementation
- `client/manager/*` — manager implementation
- `client/module/*` — module system
- `client/module/modules/*` — module implementations
- `client/module/modules/combat/*`
- `client/module/modules/dev/*`
- `client/module/modules/exploits/*`
- `client/module/modules/gui/*`
- `client/module/modules/hud/*`
- `client/module/modules/misc/*`
- `client/module/modules/movement/*`
- `client/module/modules/qwq/*`
- `client/module/modules/render/*`
- `mixin/LemonClientMixinLoader.class`
- `mixin/mixins/*` — mixin implementations
- `mixin/mixins/accessor/*` — accessor mixins

### com/lukflug/panelstudio
- `base/*`
- `component/*`
- `config/*`
- `container/*`
- `hud/*`
- `layout/*`
- `mc12/*`
- `popup/*`
- `setting/*`
- `tabgui/*`
- `theme/*`
- `widget/*`

### org/spongepowered
- `asm/*` — ASM tooling
- `tools/obfuscation/*` — obfuscation tooling

### root metadata
- `mcmod.info`
- `mixins.lemonclient.json`
- `mixins.lemonclient.refmap.json`

## Verification

Before shipping:

- Run a constant-pool or binary string scan for any remaining webhooks,
  hostnames, or download URLs.
- Confirm no Discord, Pastebin, GitHub, AI, auth, Xbox/Microsoft, Mojang,
  laby.net, or worker domains remain reachable.
- Confirm no Discord RPC native libraries remain.
- Confirm no hidden payload or compressed blobs are embedded.

## Independent audit

This repo is open for external review:

- The full cleaned source tree is in the repo.
- `SECURITY_AUDIT.md` lists what was removed.
- `webhooks.md` lists the webhook findings from the original jar.

If you want a second opinion, you can:
- Search the source tree for any remaining network code.
- Rebuild the jar from source and search it.
- Run a network-call scanner on the rebuilt jar.

Note: The original `lemon-cleaned-jar` was removed because it still contained
Discord webhook classes. Rebuild from this clean source to get a safe jar.

## Rebuilding

To rebuild a clean jar:

1. Recompile from this source.
2. Remove any classes that still reference removed packages.
3. Repackage with the correct `META-INF` and module manifest.
4. Re-verify with a network-call scanner.

## Status

Unsafe paths have been removed. This is a cleaned, auditable artifact, not a
feature-complete open-source client.
