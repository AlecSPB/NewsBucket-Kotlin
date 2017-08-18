package helpers

/**
 * Created by angad.tiwari on 11-Aug-17.
 */
class Extensions {

    /**
     * extension to swap any 2 index of the ArrayList of Generic type
     */
    public fun <T> ArrayList<T>.swap(index1: Int, index2: Int){
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }
}