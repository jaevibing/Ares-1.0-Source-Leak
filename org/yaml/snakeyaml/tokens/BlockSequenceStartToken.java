package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

public final class BlockSequenceStartToken extends Token
{
    public BlockSequenceStartToken(final Mark a1, final Mark a2) {
        super(a1, a2);
    }
    
    @Override
    public ID getTokenId() {
        /*SL:28*/return ID.BlockSequenceStart;
    }
}
