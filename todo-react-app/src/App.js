import "./App.css";
import Todo from "./Todo";
import React, { useState, useEffect } from "react";
import {
  Container,
  List,
  Paper,
  Grid,
  Button,
  AppBar,
  Toolbar,
  Typography,
} from "@mui/material";
import AddTodo from "./AddTodo";
import { call, signout } from "./ApiService.js";

function App() {
  const [items, setItems] = useState([]); // Todo 항목들을 저장할 상태 변수
  const [loading, setLoading] = useState(true); // 로딩 상태를 저장할 상태 변수

  // 페이지가 로드될 때 Todo 리스트를 가져오는 API 호출

  useEffect(() => {
    call("/todo", "GET", null).then((response) => {
      setItems(response.data);
      setLoading(false);
    }); // 가져온 데이터로 상태 업데이트 후 로딩 상태를 false로 설정
  }, []); // 빈 배열을 전달하여 페이지 로드 시 한 번만 실행

  // Todo 항목을 추가하는 API 호출
  const addItem = (item) => {
    call("/todo", "POST", item).then((response) => setItems(response.data)); // 업데이트된 Todo 리스트로 상태 업데이트
  };

  // Todo 항목을 수정하는 API 호출
  const editItem = (item) => {
    call("/todo", "PUT", item).then((response) => setItems(response.data)); // 업데이트된 Todo 리스트로 상태 업데이트
  };

  // Todo 항목을 삭제하는 API 호출
  const deleteItem = (item) => {
    call("/todo", "DELETE", item).then((response) => setItems(response.data)); // 업데이트된 Todo 리스트로 상태 업데이트
  };

  let todoItems = items.length > 0 && (
    // Todo 항목들을 보여주는 컴포넌트
    <Paper style={{ margin: 16 }}>
      <List>
        {items.map((item) => (
          <Todo
            item={item}
            key={item.id}
            editItem={editItem}
            deleteItem={deleteItem}
          />
        ))}
      </List>
    </Paper>
  );

  // navigationBar 추가
  let navigationBar = (
    <AppBar position="static">
      <Toolbar>
        <Grid justifyContent="space-between" container>
          <Grid item>
            <Typography variant="h6">오늘의 할일</Typography>
          </Grid>
          <Grid item>
            <Button color="inherit" raised onClick={signout}>
              로그아웃
            </Button>
          </Grid>
        </Grid>
      </Toolbar>
    </AppBar>
  );

  /* 로딩중이 아닐 때 렌더링 할 부분 */
  let todoListPage = (
    <div>
      {navigationBar} {/* 네비게이션 바 렌더링 */}
      <Container maxWidth="md">
        <AddTodo addItem={addItem} />
        <div className="TodoList">{todoItems}</div>
      </Container>
    </div>
  );

  /* 로딩중일 때 렌더링 할 부분 */
  let loadingPage = <h1> 로딩중.. </h1>;
  let content = loadingPage;

  if (!loading) {
    /* 로딩중이 아니면 todoListPage를 선택*/
    content = todoListPage;
  }

  /* 선택한 content 렌더링 */
  return <div className="App">{content}</div>;
}



export default App;
