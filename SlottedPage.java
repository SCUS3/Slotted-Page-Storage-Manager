package storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.tools.DocumentationTool.Location;

/**
 * A {@code SlottedPage} can store objects of possibly different sizes in a byte array.
 * 
 * @author Samuel Ginzburg
 */
public class SlottedPage implements Iterable<Object> {

	/**
	 * The ID of this {@code SlottedPage}.
	 */
	int pageID;

	/**
	 * A byte array for storing the header of this {@code SlottedPage} and objects.
	 */
	byte[] data;

	/**
	 * Constructs a {@code SlottedPage}.
	 * 
	 * @param pageID
	 *            the ID of the {@code SlottedPage}
	 * @param size
	 *            the size (in bytes) of the {@code SlottedPage}
	 */
	public SlottedPage(int pageID, int size) {
		data = new byte[size];
		this.pageID = pageID;
		setEntryCount(0);
		setStartOfDataStorage(data.length - Integer.BYTES);
	}

	@Override
	public String toString() {
		String s = "";
		for (Object o : this) {
			if (s.length() > 0)
				s += ", ";
			s += o;
		}
		return "(page ID: " + pageID + ", objects: [" + s + "])";
	}

	/**
	 * Returns the ID of this {@code SlottedPage}.
	 * 
	 * @return the ID of this {@code SlottedPage}
	 */
	public int pageID() {
		return pageID;
	}

	/**
	 * Returns the byte array of this {@code SlottedPage}.
	 * 
	 * @return the byte array of this {@code SlottedPage}
	 */
	public byte[] data() {
		return data;
	}

	/**
	 * Returns the number of entries in this {@code SlottedPage}.
	 * 
	 * @return the number of entries in this {@code SlottedPage}
	 */
	public int entryCount() {
		return readInt(0);
	}

	/**
	 * Adds the specified object in this {@code SlottedPage}.
	 * 
	 * @param o
	 *            an object to add
	 * @return the index for the object
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws OverflowException
	 *             if this {@code SlottedPage} cannot accommodate the specified object
	 */
	public int add(Object o) throws IOException, OverflowException {
		// TODO complete this method (20 points)
		// throw new UnsupportedOperationException();
		int location = save(o);
		int entryCount = entryCount();
		setEntryCount(entryCount + 1);
		saveLocation(entryCount, location);
		return entryCount;
	}
		/**
	 * Returns the object at the specified index in this {@code SlottedPage}
	 * ({@code null} if that object was removed
	 * from this {@code SlottedPage}).
	 * 
	 * @param index
	 *              an index
	 * @return the object at the specified index in this {@code SlottedPage};
	 *         {@code null} if that object was removed
	 *         from this {@code SlottedPage}
	 * @throws IndexOutOfBoundsException
	 *                                   if an invalid index is given
	 * @throws IOException
	 *                                   if an I/O error occurs
	 */
	public Object get(int index) throws IndexOutOfBoundsException, IOException {
		// TODO complete this method (20 points)
		// throw new UnsupportedOperationException();
		int location = getLocation(index);
		if (index < 0 || index >= entryCount()) {
			throw new IndexOutOfBoundsException();
		}
		if (location == -1) {
			return null;
		}
		return toObject(data, location);
	}


	/**
	 * Puts the specified object at the specified index in this {@code SlottedPage}.
	 * 
	 * @param index
	 *              an index
	 * @param o
	 *              an object to add
	 * @return the object stored previously at the specified location in this
	 *         {@code SlottedPage}; {@code null} if no
	 *         such object
	 * @throws IOException
	 *                                   if an I/O error occurs
	 * @throws OverflowException
	 *                                   if this {@code SlottedPage} cannot
	 *                                   accommodate the specified object
	 * @throws IndexOutOfBoundsException
	 *                                   if an invalid index is used
	 */
	public Object put(int index, Object o) throws IOException, OverflowException, IndexOutOfBoundsException {
		if (index == entryCount()) {
			add(o);
			return null;
		}
		Object old = get(index);
		byte[] b = toByteArray(o);
		if (old != null && b.length <= toByteArray(old).length)
			System.arraycopy(b, 0, data, getLocation(index), b.length);
		else
			saveLocation(index, save(o));
		return old;
	}

	/**
	 * Removes the object at the specified index from this {@code SlottedPage}.
	 * 
	 * @param index
	 *            an index within this {@code SlottedPage}
	 * @return the object stored previously at the specified location in this {@code SlottedPage}; {@code null} if no
	 *         such object
	 * @throws IndexOutOfBoundsException
	 *             if an invalid index is used
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public Object remove(int index) throws IndexOutOfBoundsException, IOException {
		// TODO complete this method (10 points)
		if (index < 0 || index > entryCount()) 		
			throw new UnsupportedOperationException();
		Object o = get(index);
		saveLocation(index, -1);
	return o;
	}

	/**
	 * Returns an iterator over all objects stored in this {@code SlottedPage}.
	 */
	@Override
public Iterator<Object> iterator() {
// TODO complete this method (10 points)
	ArrayList<Object> arrayList = new ArrayList<>();	
	for(int i =0; i<entryCount(); i++)
	{
		try {
			Object object = get(i);
			if(object != null)
			{
				arrayList.add(object);
			}
			
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
    return new Iterator<Object>() {
		int index = 0;
		int count = entryCount();
		/**
		 * Check if there is a non-null element after the current index.
		 * @return true if there is a non-null element, @return false otherwise
		*/
		@Override
	public boolean hasNext() {
		return index < arrayList.size();
	}
	
		@Override
			// no next non-null element after the current index = throws a UnsupportedOperationException
		public Object next() {
			if (hasNext()) {
				try {
					Object somethingBullshit = arrayList.get(index);
					index++;
					return somethingBullshit;
				} catch (Exception e) {
					// TODO: handle exception
				}
				throw new UnsupportedOperationException();
			}
			throw new NoSuchElementException();
		}
	};
}


 


	/**
	 * Reorganizes this {@code SlottedPage} to maximize its free space.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void compact() throws IOException {
		// TODO complete this method (5 points)
		throw new UnsupportedOperationException();
	}

	/**
	 * Saves the specified object in the free space of this {@code SlottedPage}.
	 * 
	 * @param o
	 *            an object
	 * @return the location at which the object is saved within this {@code SlottedPage}
	 * @throws OverflowException
	 *             if this {@code SlottedPage} cannot accommodate the specified object
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected int save(Object o) throws OverflowException, IOException {
		return save(toByteArray(o));
	}

	/**
	 * Saves the specified byte array in the free space of this {@code SlottedPage}.
	 * 
	 * @param b
	 *            a byte array
	 * @return the location at which the object is saved within this {@code SlottedPage}
	 * @throws OverflowException
	 *             if this {@code SlottedPage} cannot accommodate the specified byte array
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected int save(byte[] b) throws OverflowException, IOException {
		if (freeSpaceSize() < b.length + Integer.BYTES) {
			compact();
			if (freeSpaceSize() < b.length + Integer.BYTES)
				throw new OverflowException();
		}
		int location = startOfDataStorage() - b.length;
		System.arraycopy(b, 0, data, location, b.length);
		setStartOfDataStorage(location);
		return location;
	}

	/**
	 * Sets the number of entries in this {@code SlottedPage}.
	 * 
	 * @param count
	 *            the number of entries in this {@code SlottedPage}
	 */
	protected void setEntryCount(int count) {
		writeInt(0, count);
	}

	/**
	 * Returns the start location of the specified object within this {@code SlottedPage}.
	 * 
	 * @param index
	 *            an index that specifies an object
	 * @return the start location of the specified object within this {@code SlottedPage}
	 */
	protected int getLocation(int index) {
		return readInt((index + 1) * Integer.BYTES);
	}

	/**
	 * Saves the start location of an object within the header of this {@code SlottedPage}.
	 * 
	 * @param index
	 *            the index of the object
	 * @param location
	 *            the start location of an object within this {@code SlottedPage}
	 */
	protected void saveLocation(int index, int location) {
		writeInt((index + 1) * Integer.BYTES, location);
	}

	/**
	 * Returns the size of free space in this {@code SlottedPage}.
	 * 
	 * @return the size of free space in this {@code SlottedPage}
	 */
	public int freeSpaceSize() {
		return startOfDataStorage() - headerSize();
	}

	/**
	 * Returns the size of the header in this {@code SlottedPage}.
	 * 
	 * @return the size of the header in this {@code SlottedPage}
	 */
	protected int headerSize() {
		return Integer.BYTES * (entryCount() + 1);
	}

	/**
	 * Sets the start location of data storage.
	 * 
	 * @param startOfDataStorage
	 *            the start location of data storage
	 */
	protected void setStartOfDataStorage(int startOfDataStorage) {
		writeInt(data.length - Integer.BYTES, startOfDataStorage);
	}

	/**
	 * Returns the start location of data storage in this {@code SlottedPage}.
	 * 
	 * @return the start location of data storage in this {@code SlottedPage}
	 */
	protected int startOfDataStorage() {
		return readInt(data.length - Integer.BYTES);
	}

	/**
	 * Writes an integer value at the specified location in the byte array of this {@code SlottedPage}.
	 * 
	 * @param location
	 *            a location in the byte array of this {@code SlottedPage}
	 * @param value
	 *            the value to write
	 */
	protected void writeInt(int location, int value) {
		data[location] = (byte) (value >>> 24);
		data[location + 1] = (byte) (value >>> 16);
		data[location + 2] = (byte) (value >>> 8);
		data[location + 3] = (byte) value;
	}

	/**
	 * Reads an integer at the specified location in the byte array of this {@code SlottedPage}.
	 * 
	 * @param location
	 *            a location in the byte array of this {@code SlottedPage}
	 * @return an integer read at the specified location in the byte array of this {@code SlottedPage}
	 */
	protected int readInt(int location) {
		return ((data[location]) << 24) + ((data[location + 1] & 0xFF) << 16) + ((data[location + 2] & 0xFF) << 8)
				+ (data[location + 3] & 0xFF);
	}

	/**
	 * Returns a byte array representing the specified object.
	 * 
	 * @param o
	 *            an object.
	 * @return a byte array representing the specified object
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected byte[] toByteArray(Object o) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(b);
		out.writeObject(o);
		out.flush();
		return b.toByteArray();
	}

	/**
	 * Returns an object created from the specified byte array.
	 * 
	 * @param b
	 *            a byte array
	 * @param offset
	 *            the offset in the byte array of the first byte to read
	 * @return an object created from the specified byte array
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected Object toObject(byte[] b, int offset) throws IOException {
		try {
			if (b == null)
				return null;
			return new ObjectInputStream(new ByteArrayInputStream(b, offset, b.length - offset)).readObject();
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * A {@code OverflowException} is thrown if a {@code SlottedPage} cannot accommodate an additional object.
	 * 
	 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
	 */
	public class OverflowException extends Exception {

		/**
		 * Automatically generated serial version UID.
		 */
		private static final long serialVersionUID = -3007432568764672956L;

	}

	/**
	 * An {@code IndexOutofBoundsException} is thrown if an invalid index is used.
	 * 
	 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
	 */
	public class IndexOutOfBoundsException extends Exception {

		/**
		 * Automatically generated serial version UID.
		 */
		private static final long serialVersionUID = 7167791498344223410L;

	}

}
