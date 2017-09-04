package neon.desafio.banktransfer.repository;

import org.androidannotations.annotations.EBean;

import java.util.Arrays;
import java.util.List;

import neon.desafio.banktransfer.model.UserVO;

@EBean
public class UserRepository {

    private static List<UserVO> userList = Arrays.asList(
            new UserVO("2", "Ana", "ana@mail.com", "11 91111-1111"),
            new UserVO("3", "Bruno", "bruno@mail.com", "11 91111-2222"),
            new UserVO("4", "Caio", "caio@mail.com", "11 91111-3333"),
            new UserVO("5", "Sara", "sara@mail.com", "11 91111-4444"),
            new UserVO("6", "João", "joao@mail.com", "11 91111-5555"),
            new UserVO("7", "Karina", "karina@mail.com", "11 91111-6666"),
            new UserVO("8", "Claudio", "claudio@mail.com", "11 91111-7777"),
            new UserVO("9", "Regina", "regina@mail.com", "11 91111-8888"),
            new UserVO("10", "Mateus", "mateus@mail.com", "11 91111-9999"),
            new UserVO("11", "Rodrigo", "rodrigo@mail.com", "11 91111-0000"),
            new UserVO("12", "Daniel", "daniel@mail.com", "11 92222-1111"),
            new UserVO("13", "Leonardo", "leo@mail.com", "11 92222-2222"),
            new UserVO("14", "Elaine", "elaine@mail.com", "11 92222-3333"),
            new UserVO("15", "Lucia", "lucia@mail.com", "11 92222-4444"),
            new UserVO("16", "Lucas", "lucas@mail.com", "11 92222-5555"),
            new UserVO("17", "Felipe", "felipe@mail.com", "11 92222-6666"),
            new UserVO("18", "Guilherme", "guilherme@mail.com", "11 92222-7777"),
            new UserVO("19", "Vinicius", "vinicius@mail.com", "11 92222-8888"),
            new UserVO("20", "Tiago", "tiago@mail.com", "11 92222-9999")
    );

    public List<UserVO> getList() {
        return userList;
    }

    public UserVO getUserById(String id) {
        UserVO user = new UserVO();
        for (UserVO userItem : userList) {
            if(userItem.getId().equals(id)) {
                user = userItem;
            }
        }
        return user;
    }
}
