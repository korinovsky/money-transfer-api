package main.model;

/**
 * Class {@code Model} is a superclass for other model classes.
 */
public class Model {

    protected long id;

    public Model(long id){
        this.id = id;
    }

    public Model(){
        id = 0;
    }

    public long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Model other = (Model) obj;
        return id == other.id;
    }

}
