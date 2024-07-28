package il.meuhedet.temielderpeople.api.dto;

public class ChatGPTAnswerDTO {

    String response;

    public ChatGPTAnswerDTO(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ChatGPTAnswerModel{" +
                "response='" + response + '\'' +
                '}';
    }
}
