public class Queue<E> {
    private E [] arr;
	private int front_ptr; // index of the first element in the queue
	private int back_ptr; // index of the next insertion of the element
	private int numOfElems;
	
	public Queue(){
		this.arr = (E[]) new Object [7];
		this.back_ptr = 0;
		this.front_ptr = -1; // int equivalent of null
		this.numOfElems = 0;
	}
	
	public void enqueue(E element) {
		// O(1) - best case
		// O(N) - worst case
		// Empty and Full case
		
		if (this.numOfElems == 0) {
			this.front_ptr = 0;
			this.back_ptr = 0;
			this.arr[0] = element;
			this.back_ptr++;
			this.numOfElems++;
		}
		else {
			if (this.numOfElems == this.arr.length) {
				expand();
			}
			
			this.arr[this.back_ptr] = element;
			if (this.back_ptr == this.arr.length) { // this.back = (this.back+1)%(this.array.length);
				this.back_ptr = 0;
			}
			this.numOfElems++;
		
		}
	}
	
	public void expand() {
		E[] newArray = (E[]) new Object[this.numOfElems*2];
		int oldPos = this.front_ptr;
		for(int newPos=0; newPos<this.numOfElems; newPos++) {
			newArray[newPos] = this.arr[oldPos];
			oldPos = (oldPos+1)% (this.arr.length);
		}
		this.front_ptr = 0; // reset position
		this.back_ptr = this.numOfElems; // reset position
		this.arr = newArray;
		
	}

	
	public E dequeue() throws QueueException{ // FIFO Queue
		// 0{1)
		if(this.numOfElems == 0) {
			throw new QueueException("");
		}
		E temp = this.arr[this.back_ptr];
		this.back_ptr = (this.back_ptr-1) % this.numOfElems;  // modulo loop 
		this.numOfElems--;
		return temp;
	}

	
	public E peek() throws QueueException{
		if(this.isEmpty()) {
			throw new QueueException("");
		}
		return this.arr[this.front_ptr];
	}

	
	public boolean isEmpty() {
		return this.numOfElems == 0;
	}
	


}
