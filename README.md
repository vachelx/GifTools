# GifTools
（java）

自用的gif压缩工具类，gif解码后根据需要采样抽取部分帧并逐帧压缩，然后重组成压缩后的gif。

GifUtil.resizeGif(InputStream in, OutputStream out, int maxWidth, int maxHeight)可根据输入流压缩gif后通过输出流输出压缩后的gif
