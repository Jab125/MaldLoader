package net.fabricmc.loader.impl.game.minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

abstract class ForwardingFsProvider extends FileSystemProvider {
	static FileSystemProvider allocate(FileSystemProvider parent) {
		for (ForwardingFsProvider instance : instances) {
			if (instance != null && instance.setParent(parent)) return instance;
		}

		return null;
	}

	protected ForwardingFsProvider(int id) {
		this.id = id;

		if (id >= instances.length) instances = Arrays.copyOf(instances, Math.max(instances.length * 2, id + 1));
		if (instances[id] == null) instances[id] = this;
	}

	private final boolean setParent(FileSystemProvider parent) {
		if (this.parent != null) return false;

		this.parent = parent;

		return true;
	}

	@Override
	public final String getScheme() {
		return parent != null ? parent.getScheme() : String.format("pending%d", id);
	}

	@Override
	public final FileSystem newFileSystem(URI uri, Map<String,?> env) throws IOException {
		return parent.newFileSystem(uri, env);
	}

	@Override
	public final FileSystem getFileSystem(URI uri) {
		return parent.getFileSystem(uri);
	}

	@Override
	public final Path getPath(URI uri) {
		return parent.getPath(uri);
	}

	@Override
	public final FileSystem newFileSystem(Path path, Map<String,?> env) throws IOException {
		return parent.newFileSystem(path, env);
	}

	@Override
	public final InputStream newInputStream(Path path, OpenOption... options) throws IOException {
		return parent.newInputStream(path, options);
	}

	@Override
	public final OutputStream newOutputStream(Path path, OpenOption... options) throws IOException {
		return parent.newOutputStream(path, options);
	}

	@Override
	public final FileChannel newFileChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
		return parent.newFileChannel(path, options, attrs);
	}

	@Override
	public final AsynchronousFileChannel newAsynchronousFileChannel(Path path, Set<? extends OpenOption> options, ExecutorService executor, FileAttribute<?>... attrs) throws IOException {
		return parent.newAsynchronousFileChannel(path, options, executor, attrs);
	}

	@Override
	public final SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
		return parent.newByteChannel(path, options, attrs);
	}

	@Override
	public final DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
		return parent.newDirectoryStream(dir, filter);
	}

	@Override
	public final void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
		parent.createDirectory(dir, attrs);
	}

	@Override
	public final void createSymbolicLink(Path link, Path target, FileAttribute<?>... attrs) throws IOException {
		parent.createSymbolicLink(link, target, attrs);
	}

	@Override
	public final void createLink(Path link, Path existing) throws IOException {
		parent.createLink(link, existing);
	}

	@Override
	public final void delete(Path path) throws IOException {
		parent.delete(path);
	}

	@Override
	public final boolean deleteIfExists(Path path) throws IOException {
		return parent.deleteIfExists(path);
	}

	@Override
	public final Path readSymbolicLink(Path link) throws IOException {
		return parent.readSymbolicLink(link);
	}

	@Override
	public final void copy(Path source, Path target, CopyOption... options) throws IOException {
		parent.copy(source, target, options);
	}

	@Override
	public final void move(Path source, Path target, CopyOption... options) throws IOException {
		parent.move(source, target, options);
	}

	@Override
	public final boolean isSameFile(Path path, Path path2) throws IOException {
		return parent.isSameFile(path, path2);
	}

	@Override
	public final boolean isHidden(Path path) throws IOException {
		return parent.isHidden(path);
	}

	@Override
	public final FileStore getFileStore(Path path) throws IOException {
		return parent.getFileStore(path);
	}

	@Override
	public final void checkAccess(Path path, AccessMode... modes) throws IOException {
		parent.checkAccess(path, modes);
	}

	@Override
	public final <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
		return parent.getFileAttributeView(path, type, options);
	}

	@Override
	public final <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
		return parent.readAttributes(path, type, options);
	}

	@Override
	public final Map<String,Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
		return parent.readAttributes(path, attributes, options);
	}

	@Override
	public final void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
		parent.setAttribute(path, attribute, value, options);
	}

	public FileSystemProvider getParent() {
		return this.parent;
	}

	private static ForwardingFsProvider[] instances = new ForwardingFsProvider[10];

	private final int id;
	private FileSystemProvider parent;
}