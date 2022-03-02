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
             (year % 2 == 0)))
            return true;
        else
            return false;
    }
    
}

