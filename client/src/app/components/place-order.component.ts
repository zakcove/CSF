import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RestaurantService, OrderItem, OrderData } from '../restaurant.service';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit {
  orderItems: OrderItem[] = [];
  orderForm: FormGroup;
  grandTotal: number = 0;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private restaurantSvc: RestaurantService
  ) {
    this.orderForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.restaurantSvc.getOrderItems().subscribe(items => {
      this.orderItems = items;
      this.calculateTotal();
    });
  }

  calculateTotal(): void {
    this.grandTotal = this.orderItems.reduce((total, item) => 
      total + (item.price * item.quantity), 0);
  }

  getSubtotal(item: OrderItem): number {
    return item.price * item.quantity;
  }

  startOver(): void {
    this.restaurantSvc.setOrderItems([]);
    this.router.navigate(['/menu']);
  }

  confirmOrder(): void {
    if (this.orderForm.valid) {
      const orderData: OrderData = {
        username: this.orderForm.get('username')?.value,
        password: this.orderForm.get('password')?.value,
        items: this.orderItems
      };
      
      this.restaurantSvc.placeOrder(orderData).subscribe({
        next: (response) => {
          this.restaurantSvc.setOrderConfirmation(response);
          this.router.navigate(['/confirmation']);
        },
        error: (error) => {
          console.error('Error', error);
          if (error.status === 401) {
            alert('Invalid username and/or password');
          } else {
            alert('Failed to place order. Please try again.');
          }
        }
      });
    }
  }
}
