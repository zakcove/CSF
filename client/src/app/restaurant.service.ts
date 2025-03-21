import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';

export interface MenuItem {
  id: string;
  name: string;
  description: string;
  price: number;
}

export interface OrderItem extends MenuItem {
  quantity: number;
}

export interface OrderData {
  username: string;
  password: string;
  items: OrderItem[];
}

export interface OrderResponse {
  orderId: string;
  total: number;
  // Add other response fields as needed
}

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {
  private apiUrl = '/api';
  private orderItems = new BehaviorSubject<OrderItem[]>([]);
  private orderConfirmation = new BehaviorSubject<OrderResponse | null>(null);

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

  placeOrder(orderData: OrderData): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(`${this.apiUrl}/order`, orderData);
  }

  setOrderConfirmation(confirmation: OrderResponse): void {
    this.orderConfirmation.next(confirmation);
  }

  getOrderConfirmation(): Observable<OrderResponse | null> {
    return this.orderConfirmation.asObservable();
  }
}
