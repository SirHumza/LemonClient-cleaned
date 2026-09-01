/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.java.decompiler.main.extern.IBytecodeProvider
 *  org.jetbrains.java.decompiler.util.InterpreterUtil
 */
package org.spongepowered.asm.mixin.transformer.debug;

import java.io.File;
import java.io.IOException;
import org.jetbrains.java.decompiler.main.extern.IBytecodeProvider;
import org.jetbrains.java.decompiler.util.InterpreterUtil;

class RuntimeDecompiler.1
implements IBytecodeProvider {
    private byte[] byteCode;

    RuntimeDecompiler.1() {
    }

    public byte[] getBytecode(String externalPath, String internalPath) throws IOException {
        if (this.byteCode == null) {
            this.byteCode = InterpreterUtil.getBytes((File)new File(externalPath));
        }
        return this.byteCode;
    }
}
