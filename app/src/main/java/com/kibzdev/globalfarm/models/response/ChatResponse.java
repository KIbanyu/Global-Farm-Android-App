package com.kibzdev.globalfarm.models.response;

import java.util.List;

public class ChatResponse extends BaseResponse {

    private List<ChatMessages> data;


    public List<ChatMessages> getData() {
        return data;
    }

    public void setData(List<ChatMessages> data) {
        this.data = data;
    }

    public static class ChatMessages {

        private String message;
        private ChatUser user;
        private Long date;
        private String from;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ChatUser getUser() {
            return user;
        }

        public void setUser(ChatUser user) {
            this.user = user;
        }

        public Long getDate() {
            return date;
        }

        public void setDate(Long date) {
            this.date = date;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public static class ChatUser {
            public Long id;

            public String phone;
            public String name;
            public String photoUrl;

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhotoUrl() {
                return photoUrl;
            }

            public void setPhotoUrl(String photoUrl) {
                this.photoUrl = photoUrl;
            }
        }

    }

}
