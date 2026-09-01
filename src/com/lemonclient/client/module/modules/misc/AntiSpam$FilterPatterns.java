/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module.modules.misc;

private static class AntiSpam.FilterPatterns {
    private static final String[] ANNOUNCER = new String[]{"I just walked .+ feet!", "I just placed a .+!", "I just attacked .+ with a .+!", "I just dropped a .+!", "I just opened chat!", "I just opened my console!", "I just opened my GUI!", "I just went into full screen mode!", "I just paused my game!", "I just opened my inventory!", "I just looked at the player list!", "I just took a screen shot!", "I just swaped hands!", "I just ducked!", "I just changed perspectives!", "I just jumped!", "I just ate a .+!", "I just crafted .+ .+!", "I just picked up a .+!", "I just smelted .+ .+!", "I just respawned!", "I just attacked .+ with my hands", "I just broke a .+!", "I recently walked .+ blocks", "I just droped a .+ called, .+!", "I just placed a block called, .+!", "Im currently breaking a block called, .+!", "I just broke a block called, .+!", "I just opened chat!", "I just opened chat and typed a slash!", "I just paused my game!", "I just opened my inventory!", "I just looked at the player list!", "I just changed perspectives, now im in .+!", "I just crouched!", "I just jumped!", "I just attacked a entity called, .+ with a .+", "Im currently eatting a peice of food called, .+!", "Im currently using a item called, .+!", "I just toggled full screen mode!", "I just took a screen shot!", "I just swaped hands and now theres a .+ in my main hand and a .+ in my off hand!", "I just used pick block on a block called, .+!", "Ra just completed his blazing ark", "Its a new day yes it is", "I just placed .+ thanks to (http:\\/\\/)?DotGod\\.CC!", "I just flew .+ meters like a butterfly thanks to (http:\\/\\/)?DotGod\\.CC!"};
    private static final String[] SPAMMER = new String[]{"WWE Client's spammer", "Lol get gud", "Future client is bad", "WWE > Future", "WWE > Impact", "Default Message", "IKnowImEZ is a god", "THEREALWWEFAN231 is a god", "WWE Client made by IKnowImEZ/THEREALWWEFAN231", "WWE Client was the first public client to have Path Finder/New Chunks", "WWE Client was the first public client to have color signs", "WWE Client was the first client to have Teleport Finder", "WWE Client was the first client to have Tunneller & Tunneller Back Fill"};
    private static final String[] INSULTER = new String[]{".+ Download WWE utility mod, Its free!", ".+ 4b4t is da best mintscreft serber", ".+ dont abouse", ".+ you cuck", ".+ https://www.youtube.com/channel/UCJGCNPEjvsCn0FKw3zso0TA", ".+ is my step dad", ".+ again daddy!", "dont worry .+ it happens to every one", ".+ dont buy future it's crap, compared to WWE!", "What are you, fucking gay, .+?", "Did you know? .+ hates you, .+", "You are literally 10, .+", ".+ finally lost their virginity, sadly they lost it to .+... yeah, that's unfortunate.", ".+, don't be upset, it's not like anyone cares about you, fag.", ".+, see that rubbish bin over there? Get your ass in it, or I'll get .+ to whoop your ass.", ".+, may I borrow that dirt block? that guy named .+ needs it...", "Yo, .+, btfo you virgin", "Hey .+ want to play some High School RP with me and .+?", ".+ is an Archon player. Why is he on here? Fucking factions player.", "Did you know? .+ just joined The Vortex Coalition!", ".+ has successfully conducted the cactus dupe and duped a itemhand!", ".+, are you even human? You act like my dog, holy shit.", ".+, you were never loved by your family.", "Come on .+, you hurt .+'s feelings. You meany.", "Stop trying to meme .+, you can't do that. kek", ".+, .+ is gay. Don't go near him.", "Whoa .+ didn't mean to offend you, .+.", ".+ im not pvping .+, im WWE'ing .+.", "Did you know? .+ just joined The Vortex Coalition!", ".+, are you even human? You act like my dog, holy shit."};
    private static final String[] GREETER = new String[]{"Bye, Bye .+", "Farwell, .+"};
    private static final String[] DISCORD = new String[]{"discord.gg", "discordapp.com", "discord.io", "invite.gg"};
    private static final String[] NUMBER_SUFFIX = new String[]{".+\\d{3,}$"};
    private static final String[] GREEN_TEXT = new String[]{"^<.+> >"};
    private static final String[] TRADE_CHAT = new String[]{"buy", "sell"};
    private static final String[] WEB_LINK = new String[]{"http:\\/\\/", "https:\\/\\/", "www."};
    private static final String[] IP_ADDR = new String[]{"\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\:\\d{1,5}\\b", "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}", "^(?:http(?:s)?:\\/\\/)?(?:[^\\.]+\\.)?.*\\..*\\..*$", ".*\\..*\\:\\d{1,5}$"};
    private static final String[] IP_ADDR_AGR = new String[]{".*\\..*$"};

    private AntiSpam.FilterPatterns() {
    }

    static /* synthetic */ String[] access$000() {
        return GREEN_TEXT;
    }

    static /* synthetic */ String[] access$100() {
        return DISCORD;
    }

    static /* synthetic */ String[] access$200() {
        return WEB_LINK;
    }

    static /* synthetic */ String[] access$300() {
        return IP_ADDR;
    }

    static /* synthetic */ String[] access$400() {
        return IP_ADDR_AGR;
    }

    static /* synthetic */ String[] access$500() {
        return TRADE_CHAT;
    }

    static /* synthetic */ String[] access$600() {
        return NUMBER_SUFFIX;
    }

    static /* synthetic */ String[] access$700() {
        return ANNOUNCER;
    }

    static /* synthetic */ String[] access$800() {
        return SPAMMER;
    }

    static /* synthetic */ String[] access$900() {
        return INSULTER;
    }

    static /* synthetic */ String[] access$1000() {
        return GREETER;
    }
}
