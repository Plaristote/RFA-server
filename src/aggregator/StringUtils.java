package aggregator;

public class StringUtils {
  public static String ecmaScriptStringEscape(String str)
  {
	return (ecmaScriptStringEscape(str, '\''));
  }
	
  public static String ecmaScriptStringEscape(String str, char to_escape)
  {
	String  new_string = new String();
	boolean escaped    = false;

	for (int i = 0 ; i < str.length() ; ++i)
	{
      if (escaped)
      {
    	if (str.charAt(i) == to_escape)
    	  new_string += "\\\\";
      }
      else
      {
    	if (str.charAt(i) == to_escape)
    	  new_string += '\\';
    	else
          escaped = str.charAt(i) == '\\';
      }
      new_string += str.charAt(i);
	}
	return (new_string);
  }
  
  public static String sqlField(String str)
  {
	if (str == null)
	  return ("NULL");
	return ('\'' + ecmaScriptStringEscape(str) + '\'');
  }
}
