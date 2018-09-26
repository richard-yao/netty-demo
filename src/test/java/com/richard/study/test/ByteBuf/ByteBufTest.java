package com.richard.study.test.ByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/21 11:33
 * Description:
 */
public class ByteBufTest {

    @Test
    public void testByte() {
        byte test = 33;
        char content = (char) test;
        assertEquals('!', content);
    }

    /**
     * Unpooled provid some methods used to operate byte
     */
    @Test
    public void testByteBuffer() {
        byte[] originalByte = "Original Byte Array".getBytes();
        originalByte.clone();
        // Using copiedBuffer method will copy the specified byte array to a UnpooledHeapByteBuf object
        ByteBuf byteBuf = Unpooled.copiedBuffer(originalByte);
        // Reset the last byte to !
        originalByte[originalByte.length - 1] = 33;
        // check byteBuf content exist array or not
        if(byteBuf.hasArray()) {
            byte[] array = byteBuf.array();
            assertNotEquals(originalByte[originalByte.length - 1], array[array.length - 1]);
            // For heap ByteBuf, arrayOffset default return 0
            int offset = byteBuf.arrayOffset() + byteBuf.readerIndex();
            int length = byteBuf.readableBytes();
            assertEquals(originalByte.length, offset + length);
        }
    }
}