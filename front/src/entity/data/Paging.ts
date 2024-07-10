export default class Paging<T> {

    public  totalPage = 0
    public  totalElement = 0
    public  isFirst =true
    public  isLast = true
    public  items : T[] = []

    setItems(items: T[]) {
        this.items = items
    }
}