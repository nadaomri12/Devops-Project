package backend.stageproject.Services;

import backend.stageproject.Auth.RegisterRequestDto;

import backend.stageproject.Entity.User;
import backend.stageproject.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public User getUser(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        // Renvoie l'utilisateur trouvé s'il existe, sinon renvoie null
        return userOptional.orElse(null);    }

  /*  public List<User> getUser() {

        return userRepository.findAll();
    }
*/
  public Page<User> getUsersPage(PageRequest pageRequest) {
      return userRepository.findAll(pageRequest);
  }

    public User updateUser(UUID id, RegisterRequestDto user){
        User usertoupdate = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found with id: " + id));

        usertoupdate.setCanlogin(user.getCanlogin());
        usertoupdate.setFullName(user.getFullName());
        usertoupdate.setPathAvatar(user.getPathAvatar());
        usertoupdate.setEmail(user.getEmail());
        usertoupdate.setLanguage(user.getLanguage());
        // usertoupdate.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(usertoupdate);
    }



    public void deleteUser(UUID id) {
        boolean exist=userRepository.existsById(id);

        if (exist){
            userRepository.deleteById(id);
        }else {
            throw new IllegalStateException(
                    "User with id "+id+" does not exist "
            );
        };
    }

}
