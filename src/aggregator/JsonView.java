package aggregator;

public class JsonView
{
  private String  json       = "";
  private boolean needs_coma = false;
  
  public void property(String key, String value)
  {
    if (needs_coma) { json += ','; }
    json += '\"' + key + "\":" + (value == null ? "null" : '"' + StringUtils.ecmaScriptStringEscape(value, '"') + '"');
    needs_coma = true;
  }
  
  public void property(String key, int value)
  {
	if (needs_coma) { json += ','; }
	json += '\"' + key + "\": " + value;
    needs_coma = true;
  }
  
  public void value(String value)
  {
    if (needs_coma) { json += ','; }
    json += "\"" + StringUtils.ecmaScriptStringEscape(value, '"') + '"';
    needs_coma = true;
  }
  
  public void value(int value)
  {
    if (needs_coma) { json += ','; }
    json += value;
    needs_coma = true;
  }
  
  public void object(String key)
  {
	if (needs_coma) { json += ','; }
	json += '"' + key + "\":{";
	needs_coma = false;
  }
  
  public void object()
  {
    if (needs_coma) { json += ','; }
    json += '{';
    needs_coma = false;
  }
  
  public void end_object()
  {
	json += '}';
	needs_coma = true;
  }
  
  public void array(String key)
  {
	if (needs_coma) { json += ','; }
	json += '\"' + key + "\":[";
    needs_coma = false;
  }
  
  public void array()
  {
	if (needs_coma) { json += ','; }
	json += '[';
    needs_coma = false;
  }
  
  public void end_array()
  {
	json += ']';
	needs_coma = true;
  }
  
  public String render()
  {
	return ('{' + json + '}');
  }
}
