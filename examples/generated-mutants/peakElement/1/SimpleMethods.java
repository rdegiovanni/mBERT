package examples;

public class SimpleMethods {

    /**
     * Compute the minimum of two values
     *
     * @param a first value
     * @param b second value
     * @return a if a is lesser or equal to b, b otherwise
     */
    public int min(int a, int b) {
	    if (a <= b)
            return a;
        else 
            return b;

    }

    int gcd(int n1, int n2) {
        int gcd = 1;
        for (int i = 1; i <= n1 && i <= n2; i++) {
            if (n1 % i == 0 && n2 % i == 0) {
                gcd = i;
            }
        }
        return gcd;
    }

    boolean isLeapYear(int year) {
        // if the year is divided by 4
        if ((year % 4 == 0) &&
            ((year % 100 != 0) ||
             (year % 400 == 0)))
            return true;
        else
            return false;
    }
    

    void printArray(String[] arr) {
        for (int i = arr.length; --i >= 0; )
            print(arr[i]);
     }

    double[][] multiply(double[][] A, double[][] B) {
        double[][] result = new double[A.length][B[0].length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                double cell = 0;
                for (int i = 0; i < B.length; i++) {
                    cell += A[row][i] * B[i][col];
                }
                result[row][col] = cell;
            }
        }

        return result;
    }

    / Method to insert a new node
    public  LinkedList insert(LinkedList list, int data)
    {
        // Create a new node with given data
        Node new_node = new Node(data);
        new_node.next = null;

        // If the Linked List is empty,
        // then make the new node as head
        if (list.head == null) {
            list.head = new_node;
        }
        else {
            // Else traverse till the last node
            // and insert the new_node there
            Node last = list.head;
            while (last.next != null) {
                last = last.next;
            }

            // Insert the new_node at last node
            last.next = new_node;
        }

        // Return the list by head
        return list;
    }

    // Function to find the peak element
    // arr[]: input array
// n: size of array a[]
    public int peakElement(int[] arr,int n)
    {
        //add code here.
        int low=n;
        int high=n-1;

        while(low<=high){
            int mid=(low+high)/2;
            if(
                    (mid==0 || arr[mid]>=arr[mid-1])&&(mid==n-1 || arr[mid]>=arr[mid+1])
            ){
                return mid;
            }else if(arr[mid]<=arr[mid+1]){
                low=mid+1;
            }else{
                high=mid-1;
            }
        }
        return -1;
    }
}

}

