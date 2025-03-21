import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RestaurantService } from '../restaurant.service';

interface MenuItem {
  id: string;
  name: string;
  description: string;
  price: number;
}

interface OrderItem extends MenuItem {
  quantity: number;
}

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit {
  menuItems: MenuItem[] = [];
  selectedItems: { [key: string]: OrderItem } = {};
  totalAmount: number = 0;
  totalItems: number = 0;

  constructor(
    private restaurantSvc: RestaurantService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.restaurantSvc.getMenuItems()
      .subscribe(items => {
        this.menuItems = items;
      });
  }

  addItem(item: MenuItem): void {
    if (!this.selectedItems[item.id]) {
      this.selectedItems[item.id] = { ...item, quantity: 1 };
    } else {
      this.selectedItems[item.id].quantity++;
    }
    this.updateTotals();
  }

  removeItem(item: MenuItem): void {
    if (this.selectedItems[item.id]) {
      if (this.selectedItems[item.id].quantity > 1) {
        this.selectedItems[item.id].quantity--;
      } else {
        delete this.selectedItems[item.id];
      }
      this.updateTotals();
    }
  }

  private updateTotals(): void {
    this.totalItems = 0;
    this.totalAmount = 0;
    Object.values(this.selectedItems).forEach(item => {
      this.totalItems += item.quantity;
      this.totalAmount += item.price * item.quantity;
    });
  }

  placeOrder(): void {
    if (this.totalItems > 0) {
      this.restaurantSvc.setOrderItems(Object.values(this.selectedItems));
      this.router.navigate(['/place-order']);
    }
  }

  getQuantity(itemId: string): number | null {
    return this.selectedItems[itemId]?.quantity || null;
  }
}
