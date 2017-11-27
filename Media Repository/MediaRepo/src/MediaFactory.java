public class MediaFactory {
    //use getShape method to get object of type shape
    public Media getMedia(String mediaType){
        Subject subject = new Subject();
        
        if(mediaType == null){
            return null;
        }
        if(mediaType.equalsIgnoreCase("MUSIC")){
            return new Music(subject);
        } 
        else if(mediaType.equalsIgnoreCase("IMAGE")){
            return new Images(subject);
        } 
        else if(mediaType.equalsIgnoreCase("VIDEO")){
            return new Video(subject);
        }
        return null;
    }
}