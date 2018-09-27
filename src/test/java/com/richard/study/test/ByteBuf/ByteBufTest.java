package com.richard.study.test.ByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.junit.Ignore;
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

    @Ignore
    @Test
    public void testByte() {
        byte test = 33;
        char content = (char) test;
        assertEquals('!', content);
    }

    /**
     * Unpooled provid some methods used to operate byte
     */
    @Ignore
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

    @Test
    public void testByteBufferAttributes() {
        // create ByteBuf with initial-capacity 9, max-capacity 100
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(9, 100);
        print("allocate ByteBuf(9, 100)", byteBuf);

        // write 4 byte array, exist remain capacity
        byteBuf.writeBytes(new byte[]{1, 2, 3, 4});
        print("writeBytes(1,2,3,4)", byteBuf);

        // write a int value which will cost 4 byte into byteBuf
        byteBuf.writeInt(12);
        print("writeInt(12)", byteBuf);

        // write the last one byte, the initial-capacity costed
        byteBuf.writeBytes(new byte[]{5});
        print("writeBytes(5)", byteBuf);

        // try write another byte, but initial-capacity is empty, so byteBuf will increase capacity until the max-capacity
        byteBuf.writeBytes(new byte[]{6});
        print("writeBytes(6)", byteBuf);

        // get 方法不改变读写指针
        System.out.println("getByte(3) return: " + byteBuf.getByte(3));
        // short 类型占用2个byte
        System.out.println("getShort(3) return: " + byteBuf.getShort(3));
        // int 类型占用4个byte
        System.out.println("getInt(3) return: " + byteBuf.getInt(3));
        print("getByte()", byteBuf);

        // set 方法不改变读写指针, 虽然操作没问题，但是并不影响读写指针实际在输出时的作用范围
        int lastIdx = byteBuf.readableBytes() + 1;
        byteBuf.setByte(lastIdx, 0);
        System.out.println("getByte(" + lastIdx + ") return: " + byteBuf.getByte(lastIdx));
        print("setByte()", byteBuf);

        // read 方法改变读指针
        byte[] dst = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(dst);
        print("readBytes(" + dst.length + ")", byteBuf);
    }

    private void print(String action, ByteBuf buffer) {
        System.out.println("after ===========" + action + "============");
        System.out.println("capacity(): " + buffer.capacity());
        System.out.println("maxCapacity(): " + buffer.maxCapacity());
        System.out.println("readerIndex(): " + buffer.readerIndex());
        System.out.println("readableBytes(): " + buffer.readableBytes());
        System.out.println("isReadable(): " + buffer.isReadable());
        System.out.println("writerIndex(): " + buffer.writerIndex());
        System.out.println("writableBytes(): " + buffer.writableBytes());
        System.out.println("isWritable(): " + buffer.isWritable());
        System.out.println("maxWritableBytes(): " + buffer.maxWritableBytes());
        System.out.println();
    }
}