package com.dennisxu.lib.core.net;

import org.apache.http.entity.InputStreamEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * @Description CustomInputStreamEntity 继承自InputStreamEntity，用于Apache HttpClient实现下载进度监听
 * @author xuyang5
 * @date 2014-1-24 下午6:21:58
 */
class CustomInputStreamEntity extends InputStreamEntity {

	private final IDownloadState listener; //下载监听listener

	public CustomInputStreamEntity(InputStream instream, long length,
			final IDownloadState listener) {
		super(instream, length);
		this.listener = listener;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.listener,
				getContentLength()));
	}

	@Override
	public boolean isRepeatable() {
		return true;
	}

	public static class CountingOutputStream extends FilterOutputStream {

		private final IDownloadState listener;
		private long transferred;
		private long mContentLength;

		public CountingOutputStream(final OutputStream out,
				final IDownloadState listener, final long contentLength) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
			this.mContentLength = contentLength;
		}

		public void write(byte[] b, int off, int len) throws IOException {

			out.write(b, off, len);
			out.flush();
			if (len > 0) {
				this.transferred += len;

				this.listener
						.onProgressChanged((int)(this.transferred * 100 / mContentLength));
			}
		}

		public void write(int b) throws IOException {

			out.write(b);
			out.flush();
			this.transferred++;
		}
	}
}
