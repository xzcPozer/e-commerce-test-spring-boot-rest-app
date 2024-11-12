package com.dailycodework.dreamshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount =  BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>(); // набор используется для того, чтобы не повторялись cart_item_id

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public void addItem(CartItem item){
        this.items.add(item);
        item.setCart(this);
        updateTotalAmount();
    }

    public void removeItem(CartItem item){
        this.items.remove(item);
        item.setCart(null); // так как null - запись из таблицы cart_item будет удалена
        updateTotalAmount();
    }

    private void updateTotalAmount(){
        this.totalAmount = items.stream().map(item ->{
            BigDecimal unitPrice = item.getUnitPrice();
            if(unitPrice == null)
                return BigDecimal.ZERO;
            return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add); //reduce - складывает общую сумму и преобразует в тип BigDecimal
    }
}
