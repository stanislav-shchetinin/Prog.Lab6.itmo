package exceptions;

public class ReadTypeException extends Exception{
    public ReadTypeException(Class clazz, int numberWord, String word){
        super(
                String.format("Не %s: (%d - номер слова, \"%s\" - слово)", clazz.getSimpleName(), numberWord, word)
        );
    }
}
