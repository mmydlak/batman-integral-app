package batmanintegral.model;

public class NewPointEvent {

    public FPoint point;
    public boolean belongsToFunction;

//    public NewPointEvent() {
//        point = null;
//        belongsToFunction = false;
//    }

    public NewPointEvent(FPoint point, boolean belongsToFunction) {
        this.point = point;
        this.belongsToFunction = belongsToFunction;
    }

}
