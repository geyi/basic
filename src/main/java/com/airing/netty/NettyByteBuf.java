package com.airing.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

public class NettyByteBuf {

    public static void main(String[] args) {
        // 缓冲区的分配方式
//        PooledByteBufAllocator.DEFAULT.buffer();
//        UnpooledByteBufAllocator.DEFAULT.buffer();

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(4, 16);
        print(buffer);

        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print(buffer);

        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print(buffer);

        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print(buffer);

        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print(buffer);

        // 超出最大容量时抛出异常
//        buffer.writeBytes(new byte[]{1, 2, 3, 4});
//        print(buffer);
    }

    public static void print(ByteBuf buf) {
        System.out.println("是否使用的是直接内存：" + buf.isDirect());
        System.out.println("容量：" + buf.capacity());
        System.out.println("最大容量：" + buf.maxCapacity());

        System.out.println("是否可读：" + buf.isReadable());
        System.out.println("可读的字节数：" + buf.readableBytes());
        System.out.println("读索引的位置：" + buf.readerIndex());

        System.out.println("是否可写：" + buf.isWritable());
        System.out.println("可写的字节数：" + buf.writableBytes());
        System.out.println("写索引的位置：" + buf.writerIndex());

        System.out.println("----------------------");
    }

}
