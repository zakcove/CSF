import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';

interface MenuItem {
  id: string;
  name: string;
  description: string;
  price: number;
}

interface OrderItem extends MenuItem {
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {
  private apiUrl = '/api';
  private orderItems = new BehaviorSubject<OrderItem[]>([]);

  constructor(private http: HttpClient) { }

  getMenuItems(): Observable<MenuItem[]> {
    return this.http.get<MenuItem[]>(`${this.apiUrl}/menu`);
  }

  setOrderItems(items: OrderItem[]): void {
    this.orderItems.next(items);
  }

  getOrderItems(): Observable<OrderItem[]> {
    return this.orderItems.asObservable();
  }
}
