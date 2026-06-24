package com.example.banking_app.service.impl;

import com.example.banking_app.dto.AccountDto;
import com.example.banking_app.entity.Account;
import com.example.banking_app.entity.Transaction;
import com.example.banking_app.exception.ResourceNotFoundException;
import com.example.banking_app.mapper.AccountMapper;
import com.example.banking_app.repository.AccountRepository;
import com.example.banking_app.repository.TransactionRepository;
import com.example.banking_app.service.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.accountRepository=accountRepository;
        this.transactionRepository=transactionRepository;
    }
    

    @Override
    public AccountDto createAccount(AccountDto accountDto)
    {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {

       Account account =  accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exists"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exists"));
        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exists"));

        if(account.getBalance() < amount){
            throw new ResourceNotFoundException("Insufficient amount");
        }

        double total = account.getBalance() - amount;

        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account))
                .collect(Collectors.toList());


    }

    @Override
    public void deleteAccount(Long id) {
        Account account =  accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exists"));

        accountRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void transferMoney(Long fromAccountId, Long toAccountId, Double amount) {
        if(fromAccountId == null ||
                toAccountId == null ||
                amount == null){

            throw new ResourceNotFoundException(
                    "Transfer data missing");
        }

        Account sender = accountRepository.findById(fromAccountId).orElseThrow(()-> new RuntimeException("Sender not found"));
        Account receiver = accountRepository.findById(toAccountId).orElseThrow(()-> new RuntimeException("Receiver not found"));
        if(sender.getBalance()<amount){
            throw new ResourceNotFoundException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance()-amount);

        accountRepository.save(sender);

        receiver.setBalance(receiver.getBalance()+amount);

        accountRepository.save(receiver);

        //save transaction during transfer
        Transaction senderTxn = new Transaction();
        senderTxn.setAccountId(fromAccountId);
        senderTxn.setAmount(amount);
        senderTxn.setType("TRANSFER_SENT");
        senderTxn.setStatus("SUCCESS");
        senderTxn.setTimestamp(LocalDateTime.now());

        Transaction receiverTxn = new Transaction();
        receiverTxn.setAccountId(toAccountId);
        receiverTxn.setAmount(amount);
        receiverTxn.setType("TRANSFER_RECEIVED");
        receiverTxn.setStatus("SUCCESS");
        receiverTxn.setTimestamp(LocalDateTime.now());

        transactionRepository.save(senderTxn);
        transactionRepository.save(receiverTxn);


    }

    // Inject Repository into accountserviceimpl

    @Override
    public List<Transaction> getTransactions(Long accountId){
        return transactionRepository.findByAccountId(accountId);
    }
}
