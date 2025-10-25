package com.haui.bookinghotel.service;

import com.haui.bookinghotel.domain.User;
import com.haui.bookinghotel.domain.dto.Meta;
import com.haui.bookinghotel.domain.dto.ResUserDTO;
import com.haui.bookinghotel.domain.dto.ResultPaginationDTO;
import com.haui.bookinghotel.repository.UserRepository;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResultPaginationDTO handleFetchAllUsers(@Filter Specification<User> spec, Pageable pageable){
        Page<User> pageUser = this.userRepository.findAll(spec,pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        Meta meta= new Meta();
        meta.setPage(pageable.getPageNumber() +1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        res.setMeta(meta);

        List<ResUserDTO> users = pageUser.getContent().stream()
                            .map(item -> new ResUserDTO(
                                    item.getId(),
                                    item.getUsername(),
                                    item.getEmail(),
                                    item.getPhoneNumber()
                            ))
                            .toList();
        res.setResult(users);
        return res;
    }
    public User handleFetchUserById(Long id){
        Optional<User> user = this.userRepository.findById(id);
        return user.orElse(null);
    }

    public User handleCreateUser(User user){
        return this.userRepository.save(user);
    }

    public User handleUpdateUser(User user){
        Optional<User> oldUser = this.userRepository.findById(user.getId());
        if(oldUser.isPresent()){
            User newUser = oldUser.get();
            newUser.setUsername(user.getUsername());
            newUser.setPassword(user.getPassword());
            newUser.setEmail(user.getEmail());
            newUser.setPhoneNumber(user.getPhoneNumber());
            return userRepository.save(newUser);
        }
        return null;
    }

    public void handleDeleteUser(Long id){
        this.userRepository.deleteById(id);
    }

    public User handleFindUserByUsername(String username){
        return this.userRepository.findByEmail(username);
    }
}
